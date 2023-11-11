package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.dto.UserDTO;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.nidhishah.meetingscheduler.services.OrganizationService;
import org.nidhishah.meetingscheduler.services.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrganizationController {

    private TeamMemberService teamMemberService;
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(TeamMemberService teamMemberService,
                                  OrganizationService organizationService) {

        this.teamMemberService = teamMemberService;
        this.organizationService = organizationService;
    }

    @GetMapping("/orgsetup")
    public String showOrgSetupForm(Model model){
        model.addAttribute("teammember",new UserDTO());
        model.addAttribute("organization", new OrganizationDTO());

        return "orgadmin_setup_page";

    }

    @PostMapping("/orgsetupprocess")
    public String processOrgSetup(@ModelAttribute ("teammember") UserDTO userDTO,
                                  BindingResult bindingResult,
                                  @ModelAttribute ("organization") OrganizationDTO organizationDTO,
                                  BindingResult bindingResultOrganization, Model model)
    {
        try{
            teamMemberService.registerAdmin(userDTO, organizationDTO);
            return "redirect:/" + "setorgdetail/"+ organizationDTO.getOrgName() ;
        }catch (Exception e){
            String errormessage = "Organization setup failed. The provided organization name is already in use.";
            System.out.println(errormessage);
             model.addAttribute("errorMessage",errormessage);
             return "orgadmin_setup_page";
        }

    }

    @GetMapping("/setorgdetail/{organizationName}")
    public String updateOrgWithExtraDetails(@PathVariable(name="organizationName") String organizationName,
                                            Model model)
    {
        System.out.println("Inside the setorgdetail controller");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                if (userPrincipal.getOrganizationName().equals(organizationName)) {
                    // Access the organization name from the UserPrincipal
                    System.out.println("Authenticated User organization" + userPrincipal.getOrganizationName());
                    System.out.println(userPrincipal.getAuthorities());
                    //get organization: if is null //404 error
                    OrganizationDTO organizationDTO = organizationService.findByOrgName(organizationName);
                    if (organizationDTO != null) {
                        model.addAttribute("organization", organizationDTO);
                        return "organization_detail_setup";
                    } else {
                        throw new Exception("No organization found.");
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Error: 404");
            return "Error/404";
        }
        return "Error/404";
    }

    @PostMapping("/process-orgdetailsetup")
    public String processOrgDetailAddition(@ModelAttribute(name="organization") OrganizationDTO organizationDTO,
                                           BindingResult bindingResult, Model model)
    {
        try{
            //access the authenticated user information
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                System.out.println(authentication.getPrincipal());
                System.out.println(authentication.getDetails());

                System.out.println("In Controller: "+organizationDTO.getOrgName());
                if(organizationService.setOrganizationDetail(organizationDTO)){
                    // update is done - give admin a new page for setting availability
                    return "redirect:/availability_setup";

                }
                else{
                    //update not done - throw error
                    model.addAttribute("errorMessage"," Something went wrong. Please try again later.");
                    return "organization_detail_setup";
                }
            }

        }catch (Exception e){
            System.out.println("Error: 404");
            return "Error/404";
        }
        return "Error/404";
    }
}
