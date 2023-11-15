/**********Team Member - create,
 *         Team member Availability - set, get, update, delete
 * ******************/
package org.nidhishah.meetingscheduler.controller;

import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;
import org.nidhishah.meetingscheduler.dto.DaysAvailabilityDTO;
import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.entity.TeamMemberExtraInfo;
import org.nidhishah.meetingscheduler.repository.TeamMemberExtraInfoRepository;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.nidhishah.meetingscheduler.services.TeamMemberServiceImpl;
import org.nidhishah.meetingscheduler.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TeamMemberController {
    private static final Logger logger = LoggerFactory.getLogger(TeamMemberController.class.getName());
    private UserServiceImpl userService;
    private TeamMemberServiceImpl teamMemberService;

    private TeamMemberExtraInfoRepository teamMemberExtraInfoRepository;
    @Autowired
    public TeamMemberController(UserServiceImpl userService, TeamMemberServiceImpl teamMemberService,
                                TeamMemberExtraInfoRepository teamMemberExtraInfoRepository) {
        this.userService = userService;
        this.teamMemberService = teamMemberService;
        this.teamMemberExtraInfoRepository = teamMemberExtraInfoRepository;
    }

    /////////////////////////ADD TEAM MEMBER BY ADMIN
    @PostMapping("/addteammember")
    public String addTeamMember(Model model, @ModelAttribute("newmember") NewOrgMemberDTO newOrgMemberDTO,
                                @RequestParam String roleName, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Inside the add team member");
            // get authenticated admin organization
            //check the teammember is already with organization or not. either as client, teammember or as admin
            // if is there, send error, this teammember is already exist with your organization
            // else: save this person and generate one time code and send email to the person and refresh the list in admin page
            //return the admin page.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                //organization information collection
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String adminOrganization = userPrincipal.getOrganizationName();
                //Find that the new member is present in that organization or not
                //new member found
                if(userService.findUserByEmailAndOrganization(newOrgMemberDTO.getEmail(), adminOrganization)){
                    redirectAttributes.addFlashAttribute("teamAddError",newOrgMemberDTO.getEmail() + " is already member of "+ adminOrganization);
                    return "redirect:/adm_dashboard";
                }
                else{
                    //new member not found, so add to organization
                    teamMemberService.registerNewTeamMember(newOrgMemberDTO,adminOrganization);
                    redirectAttributes.addFlashAttribute("teamAddSuccess","New member added successfully and email sent.");
                    return "redirect:/adm_dashboard";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return "error_404";
    }

    //////// TEAM MEMBER AVAILABILITY SETUP - BY TEAM MEMBER OR ADMIN
    @GetMapping("/availability_setup")
    public String getAvailabilitySetupForm(Model model){

        try{
            logger.info("AVAILABILITY SETUP PAGE REQUESTED.");
            //get authenticated person info
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                //organization information collection
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                // check if there is extra information already there for teammember
                //if so map it to DTO
                TeamMemberExtraInfo teamMemberExtraInfo =teamMemberExtraInfoRepository.getTeamMemberExtraInfoByUser_Id(userPrincipal.getUserId());
                TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
                if (teamMemberExtraInfo !=null){
                    teamMemberDTO.setOnSiteMeetingAvailable(teamMemberExtraInfo.isOnSiteMeetingAvailable());
                    teamMemberDTO.setZoomMeetingAvailable(teamMemberExtraInfo.isZoomMeetingAvailable());
                }

                teamMemberDTO.setOrgName(userPrincipal.getOrganizationName());
                // if teammember have the availability get them or get default one
                DaysAvailabilityDTO availabilityDTO = teamMemberService.getTeamMemberAvailability(userPrincipal.getUsername(),userPrincipal.getOrganizationName());

                //teammemberdto for getting meeting time, meeting type. zoomlink thing
                model.addAttribute("teammeber",teamMemberDTO);
                model.addAttribute("daysavail",availabilityDTO);
                return "availability_setup_page";
            }
            else{throw new Exception();}
        }catch (Exception e){
            e.printStackTrace();
            logger.error("USER IS AUTHENTICATE OR NOT ADMIN OR TEAMMEMBER..");
            return "login";
        }
    }

    //////// SET, UPDATE, DELETE Availability
    @PostMapping("/saveavailability")
    public String setTeamMemberAvailability(@ModelAttribute(name="teammeber") TeamMemberDTO teamMemberDTO,
                                          @ModelAttribute(name="daysavail") DaysAvailabilityDTO availabilityDTO) {
        try {
            //////////////////////////////////////////////////////////////////////////////////
            logger.info("::::Set Availability for Team MEmber::::::");

            //get authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                //organization information collection
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                //now get the user id
                Long userid = userPrincipal.getUserId();
                //Now save updated or new availability
                Boolean successSetup = teamMemberService.setTeamMemberAvailability(userid, teamMemberDTO, availabilityDTO);
                logger.debug("CONTROLLER RECEIVE SUCESSSETUP: "+ successSetup);
                if (successSetup) {
                    String role = userPrincipal.getAuthorities().isEmpty() ? "" : userPrincipal.getAuthorities().iterator().next().getAuthority();
                    if (role.equals("admin")) {
                        return "redirect:/adm_dashboard";
                    } else if (role.equals("teammember")) {
                        return "teammember_page";
                    }
                } else {
                    throw new Exception();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
             logger.debug("UNSUCESSFUL SETUP");
            //return the availability form again with message
        }
        return "redirect:/availability_setup";
    }

    @GetMapping("/team-dashboard")
    public String getTeamMemberDashboard(){
        return "teammember_page";
    }
}
