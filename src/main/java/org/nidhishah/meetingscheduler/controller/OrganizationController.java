package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.services.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        return "orgadmin_setup_page";
    }

    @PostMapping("/process-orgsetup")
    public String processOrgSetup(@ModelAttribute("teammember") TeamMemberDTO teamMemberDTO){

        // call teammember service for organization creation and admin creation

//        System.out.println(teamMemberDTO.getOrganizationName());
//        System.out.println(teamMemberDTO.getEmail());
//        System.out.println(teamMemberDTO.getFirstName());
//        System.out.println(teamMemberDTO.getLastName());
//        System.out.println(teamMemberDTO.getPassword());

        teamMemberService.registerAdmin(teamMemberDTO);

        return "file";
    }
}
