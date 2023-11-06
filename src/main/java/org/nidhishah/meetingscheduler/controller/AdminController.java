package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.ClientDTO;
import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;

import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.entity.User;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.nidhishah.meetingscheduler.services.ClientServiceImpl;
import org.nidhishah.meetingscheduler.services.OrganizationService;
import org.nidhishah.meetingscheduler.services.OrganizationServiceImpl;
import org.nidhishah.meetingscheduler.services.TeamMemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@Controller
public class AdminController {

    private OrganizationServiceImpl organizationService;
    private UserRepository userRepository;

    private TeamMemberServiceImpl teamMemberService;

    private ClientServiceImpl clientService;

    @Autowired
    public AdminController(OrganizationServiceImpl organizationService, UserRepository userRepository
                           , TeamMemberServiceImpl teamMemberService,ClientServiceImpl clientService) {
        this.organizationService = organizationService;
        this.userRepository = userRepository;
        this.teamMemberService = teamMemberService;
        this.clientService = clientService;
    }

    @GetMapping("/adm_dashboard")
    public String GetAdminDashboard(Model model, @ModelAttribute(name="teamAddError")String teamAddError,
                                    @ModelAttribute(name="teamAddSuccess")String teamAddSuccess) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String adminOrganization = userPrincipal.getOrganizationName();
                System.out.println("authenticated adminORganization: " + adminOrganization);
                // find the organization
                OrganizationDTO organizationDTO = organizationService.findByOrgName(adminOrganization);
                System.out.println("Find ORganization details: " + organizationDTO.getOrgDescription());
                System.out.println(organizationDTO.getOrgName() + "-> Address->" + organizationDTO.getOrgAddress1() +
                        organizationDTO.getOrgAddress2() + organizationDTO.getOrgCity() + organizationDTO.getOrgState()
                        + organizationDTO.getOrgState() + "--->Contact: " + organizationDTO.getOrgContact());

                if (organizationDTO != null) {
                    //get list of teammembers for given organzation
                    List<TeamMemberDTO> teamMemberDTOList = teamMemberService.getTeamMembersByOrgName(adminOrganization);

                    for (TeamMemberDTO member:teamMemberDTOList){
                        System.out.println("In Controller:::: "+member);
                    }
                    // get list of clients
                    List<ClientDTO> clientDTOList = clientService.getClientListByOrgName(adminOrganization);

                    //add dto with model
                    model.addAttribute("organization",organizationDTO);
                    model.addAttribute("teammembers",teamMemberDTOList);
                    model.addAttribute("clients",clientDTOList);
                    model.addAttribute("newmember", new NewOrgMemberDTO());
                    //if redirected,, check messages, if is there add it


                    if(!teamAddError.isEmpty()){
                        System.out.println("teamaddError:  "+teamAddError + " "+ teamAddError.isEmpty());
                        model.addAttribute("teamadderror",teamAddError);
                    } else if (!teamAddSuccess.isEmpty()) {
                        model.addAttribute("teamaddsuccess",teamAddSuccess);

                    }
                    //get the page
                    return "admin_dashboard";
                } else {
                    throw new Exception("Error..");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();;
            return "file";
        }
        return "file";
    }

}
