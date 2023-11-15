/**************ORGANIZATION CREATE, UPDATE FROMS AND PROCESSES -NIDHI SHAH******************/
package org.nidhishah.meetingscheduler.controller;

import jakarta.validation.Valid;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.UserDTO;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.nidhishah.meetingscheduler.services.OrganizationService;
import org.nidhishah.meetingscheduler.services.TeamMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class.getName());
    @Autowired
    public OrganizationController(TeamMemberService teamMemberService,
                                  OrganizationService organizationService) {

        this.teamMemberService = teamMemberService;
        this.organizationService = organizationService;
    }

    ////////////////// ORGANIZATION SETUP PAGE
    @GetMapping("/orgsetup")
    public String showOrgSetupForm(Model model){
        model.addAttribute("teammember",new UserDTO());
        model.addAttribute("organization", new OrganizationDTO());

        return "orgadmin_setup_page";

    }

    ////////// ORGANIZATION SETUP PROCESS -IF ORGANIZATION IS ALREADY THERE _ GIVE ERROR
    @PostMapping("/orgsetupprocess")
    public String processOrgSetup(@Valid @ModelAttribute ("teammember") UserDTO userDTO,
                                  BindingResult bindingResult,
                                  @ModelAttribute ("organization") OrganizationDTO organizationDTO,
                                   Model model)
    {
        // Validate UserDTO
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                logger.error(error.getDefaultMessage());
            });
            // Handle validation errors for UserDTO
            return "orgadmin_setup_page";
        }
        try{
            teamMemberService.registerAdmin(userDTO, organizationDTO);
            return "redirect:/" + "setorgdetail/"+ organizationDTO.getOrgName() ;
        }catch( Exception e){

            String errormessage = "Organization setup failed. The provided organization name is already in use.";
             model.addAttribute("errorMessage",errormessage);
             return "orgadmin_setup_page";
        }

    }

    ////////////////////////////////////// SET ORGANIZATION DETAIL FORM
    @GetMapping("/setorgdetail/{organizationName}")
    public String updateOrgWithExtraDetails(@PathVariable(name="organizationName") String organizationName,
                                            Model model)
    {
        logger.info("Inside the setorgdetail controller");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                if (userPrincipal.getOrganizationName().equals(organizationName)) {
                    // Access the organization name from the UserPrincipal
                    logger.info("Get form for organization Extra Information setup for given url - organization.");
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
            logger.error("ERROR: 404");
            return "error_404";
        }
        return "error_404";
    }

    ////////////////////////////////////// PROCESS ORGANIZATION DETAIL FORM
    @PostMapping("/process-orgdetailsetup")
    public String processOrgDetailAddition(@ModelAttribute(name="organization") OrganizationDTO organizationDTO,
                                           Model model)
    {
        try{
            //access the authenticated user information
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
               logger.info("process organization Extra Information setup form for organization"+ organizationDTO.getOrgName());

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
            logger.error("ERROR: 404");
            return "error_404";
        }
        return "error_404";
    }
}
