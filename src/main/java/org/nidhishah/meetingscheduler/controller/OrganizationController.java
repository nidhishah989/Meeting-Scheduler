package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.services.OrganizationService;
import org.nidhishah.meetingscheduler.services.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
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
            return "redirect:/" + organizationDTO.getOrgName() + "/setorgdetail";
        }catch (Exception e){
            String errormessage = "Organization setup failed. The provided organization name is already in use.";
            System.out.println(errormessage);
             model.addAttribute("errorMessage",errormessage);
             return "orgadmin_setup_page";
        }

    }

    @GetMapping("/{organizationName}/setorgdetail")
    public String updateOrgWithExtraDetails(@PathVariable(name="organizationName") String organizationName,
                                            Model model)
    {
        //get organization: if is null //404 error
        OrganizationDTO organizationDTO = organizationService.findByOrgName(organizationName);
        if(organizationDTO != null){
            model.addAttribute("organization",organizationDTO);
            return "organization_detail_setup";
        }
        else{
            System.out.println("Error: 404");
            return "error/404";
        }
    }

    @PostMapping("/process-orgdetailsetup")
    public String processOrgDetailAddition(@ModelAttribute(name="organization") OrganizationDTO organizationDTO,
                                           BindingResult bindingResult, Model model)
    {
        try{
            System.out.println("In Controller: "+organizationDTO.getOrgName());
            if(organizationService.setOrganizationDetail(organizationDTO)){
                // update is done - give admin a new page for setting availability
                return "file";

            }
            else{
                //update not done - throw error
                model.addAttribute("errorMessage"," Something went wrong. Please try again later.");
                return "organization_detail_setup";
            }
        }catch (Exception e){

        }
        return "file";
    }
}
