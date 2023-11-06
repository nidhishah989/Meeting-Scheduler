package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.SignUPDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class LoginController {

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
    public String getSignUpPage(Model model){
      //pass the logindto
      model.addAttribute("signupuser",new SignUPDTO());
      return "sign_up";
    }

    @PostMapping("/signup")
    public String processSignUp(@ModelAttribute(name="signupuser") SignUPDTO signUPDTO){

        System.out.println(signUPDTO.toString());
        return "file";
    }

}
