package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.SignUPDTO;
import org.nidhishah.meetingscheduler.services.OrganizationServiceImpl;
import org.nidhishah.meetingscheduler.services.UserServiceImpl;
import org.nidhishah.meetingscheduler.services.UserSignUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Array;
import java.util.Arrays;

@Controller
public class LoginController {

    private OrganizationServiceImpl organizationService;
    private UserServiceImpl userService;

    private final UserSignUpServiceImpl userSignUpService;

    @Autowired
    public LoginController(OrganizationServiceImpl organizationService, UserServiceImpl userService,
                           UserSignUpServiceImpl userSignUpService) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.userSignUpService = userSignUpService;
    }

    @GetMapping("/login")
    public String getLoginPage()
    {
        return "login";
    }


    @PostMapping("/login")
    public String loginprocess()
    {
        return "file";
    }

    @GetMapping("/signup")
    public String getSignUpPage(Model model,@ModelAttribute(name="Error") String Error){
      //pass the logindto
      model.addAttribute("signupuser",new SignUPDTO());
      //if error is not empty - add it
      if(!Error.isEmpty()){
          System.out.println("Error: "+ Error);
          model.addAttribute("error",Error);
      }
      return "sign_up";
    }

    @PostMapping("/signup")
    public String processSignUp(@ModelAttribute(name="signupuser") SignUPDTO signUPDTO,
                                RedirectAttributes redirectAttributes){
        try {
            System.out.println("Sign up email: " + signUPDTO.getEmail());
            System.out.println("Sign up temppasscode: " + signUPDTO.getTempPasccode());
            System.out.println("Sign up organization: " + signUPDTO.getOrganization());
            // Check organization presents.
            if(organizationService.findByOrgName(signUPDTO.getOrganization())!= null) {
                //find user already there for given organization
                if (userService.findUserByEmailAndOrganization(signUPDTO.getEmail(), signUPDTO.getOrganization())) {
                    // complete signup process - if not empty- as per rolename returned, redirect user
                    String roleName = userSignUpService.completeUserSignUpProcess(signUPDTO);
                    if (!roleName.isEmpty()) {
                        if (roleName.equals("teammember")) {
                            //redirect for availability setup
                            return "file";
                        } else if (roleName.equals("client")) {
                            //redirect for meeting schedule page
                            return "file";
                        }
                    }
//                    else{
//                        throw new Exception();
//                    }
//                }
//                else{
//                    throw new Exception();
//                }
                }
            }
            //Either organization not there, or user is not there, or something went wrong in user Signup
            throw new Exception();
        }catch (Exception e){
            //add message and show sign up page again
            System.out.println("Unsuccessful Sign up");
            redirectAttributes.addFlashAttribute("Error","Unsuccessful Sign Up. Confirm your information. Already Sign Up! Login");
        }
        return "redirect:/signup";
    }

}
