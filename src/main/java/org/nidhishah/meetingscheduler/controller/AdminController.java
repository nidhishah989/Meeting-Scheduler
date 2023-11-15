/*************ADMIN DASHBOARD _ BY NIDHI SHAH ************/
package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.ClientDTO;
import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.nidhishah.meetingscheduler.services.ClientServiceImpl;
import org.nidhishah.meetingscheduler.services.OrganizationServiceImpl;
import org.nidhishah.meetingscheduler.services.TeamMemberServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.util.List;

@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class.getName());
    private final OrganizationServiceImpl organizationService;
//    private UserRepository userRepository;

    private final TeamMemberServiceImpl teamMemberService;

    private final ClientServiceImpl clientService;

    @Autowired
    public AdminController(OrganizationServiceImpl organizationService
                           , TeamMemberServiceImpl teamMemberService,ClientServiceImpl clientService) {
        this.organizationService = organizationService;
        this.teamMemberService = teamMemberService;
        this.clientService = clientService;
    }

    ////////////////////////ADMIN DASHBOARD ///////////////
    @GetMapping("/adm_dashboard")
    public String GetAdminDashboard(Model model, @ModelAttribute(name="teamAddError")String teamAddError,
                                    @ModelAttribute(name="teamAddSuccess")String teamAddSuccess,
                                    @ModelAttribute(name="clientAddError")String clientAddError,
                                    @ModelAttribute(name="clientAddSuccess")String clientAddSuccess) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String adminOrganization = userPrincipal.getOrganizationName();

                logger.info("Authenticated User: "+ userPrincipal.getUsername() + "from " + adminOrganization + "organization");

                // find the organization
                OrganizationDTO organizationDTO = organizationService.findByOrgName(adminOrganization);

                if (organizationDTO != null) {
                    logger.debug("Organization from authenticated user presents in organization table. Good to Go.");
                    //get list of teammembers for given organzation
                    List<TeamMemberDTO> teamMemberDTOList = teamMemberService.getTeamMembersByOrgName(adminOrganization);

                    for (TeamMemberDTO member:teamMemberDTOList){
                        logger.debug("TEAM MEMBER::::: "+member);
                    }
                    // get list of clients
                    List<ClientDTO> clientDTOList = clientService.getClientListByOrgName(adminOrganization);

                    //add dto with model
                    model.addAttribute("organization",organizationDTO);
                    model.addAttribute("teammembers",teamMemberDTOList);
                    model.addAttribute("clients",clientDTOList);
                    model.addAttribute("newmember", new NewOrgMemberDTO());
                    //if redirected, check messages, if is there add it


                    if(!teamAddError.isEmpty()){
                        logger.error("teamaddError:  "+teamAddError);
                        model.addAttribute("teamadderror",teamAddError);
                    } else if (!teamAddSuccess.isEmpty()) {
                        model.addAttribute("teamaddsuccess",teamAddSuccess);
                    }
                    else if(!clientAddError.isEmpty()){
                        logger.error("clientaddError:  "+clientAddError);
                        model.addAttribute("clientadderror",clientAddError);
                    } else if (!clientAddSuccess.isEmpty()) {
                        model.addAttribute("clientaddsuccess",clientAddSuccess);
                    }
                    //get the page
                    return "admin_dashboard";
                } else {
                    throw new Exception("Error..");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error_404";
        }
        return "error_404";
    }

}
