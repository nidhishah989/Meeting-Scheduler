package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.services.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrganizationController {

    private TeamMemberService teamMemberService;

    @Autowired
    public OrganizationController(TeamMemberService teamMemberService) {

        this.teamMemberService = teamMemberService;
    }

    @GetMapping("/orgsetup/")
    public String showOrgSetupForm(Model model){
        model.addAttribute("teammember",new TeamMemberDTO());
        model.addAttribute("organization", new OrganizationDTO());

        return "orgadmin_setup_page";

    }

    @PostMapping("/process-orgsetup")
    public String processOrgSetup(@ModelAttribute ("teammember") TeamMemberDTO teamMemberDTO,
                                  BindingResult bindingResult,
                                  @ModelAttribute ("organization") OrganizationDTO organizationDTO,
                                  BindingResult bindingResultOrganization, Model model)
    {
        try{
            teamMemberService.registerAdmin(teamMemberDTO, organizationDTO);
            return "file";
        }catch (Exception e){
            String errormessage = "Organization setup failed. The provided organization name is already in use.";
            System.out.println(errormessage);
             model.addAttribute("errorMessage",errormessage);
             return "orgadmin_setup_page";
        }


    }
}
