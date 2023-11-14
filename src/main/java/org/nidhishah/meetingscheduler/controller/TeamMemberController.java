package org.nidhishah.meetingscheduler.controller;

import jakarta.validation.constraints.Null;
import org.nidhishah.meetingscheduler.dto.DaysAvailabilityDTO;
import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.dto.TimeSlot;
import org.nidhishah.meetingscheduler.entity.TeamMemberExtraInfo;
import org.nidhishah.meetingscheduler.repository.TeamMemberExtraInfoRepository;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.nidhishah.meetingscheduler.services.TeamMemberServiceImpl;
import org.nidhishah.meetingscheduler.services.UserServiceImpl;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeamMemberController {

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

    @PostMapping("/addteammember")
    public String addTeamMember(Model model, @ModelAttribute("newmember") NewOrgMemberDTO newOrgMemberDTO,
                                @RequestParam String roleName, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Inside the add team member");
            System.out.println(newOrgMemberDTO.getEmail());
            System.out.println(newOrgMemberDTO.getFirstName());
            System.out.println(newOrgMemberDTO.getLastName());
            System.out.println(roleName); // roleName will be "teammember" here
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
                //Find that the new member is present in that organization or not

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return "file";
    }

    @GetMapping("/availability_setup")
    public String getAvailabilitySetupForm(Model model){

        try{
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
//                System.out.println("Sunday end time: "+ availabilityDTO.getSundayTimeSlot().get(0).getEndTime());
                //teammemberdto for getting meeting time, meeting type. zoomlink thing
                model.addAttribute("teammeber",teamMemberDTO);
                model.addAttribute("daysavail",availabilityDTO);
                return "availability_setup_page";
            }
            else{throw new Exception();}
        }catch (Exception e){
            e.printStackTrace();
            return "login";
        }
    }

    @PostMapping("/saveavailability")
    public String setTeamMemberAvailability(@ModelAttribute(name="teammeber") TeamMemberDTO teamMemberDTO,
                                          @ModelAttribute(name="daysavail") DaysAvailabilityDTO availabilityDTO) {
        try {
            //////////////////////////////////////////////////////////////////////////////////
            System.out.println("::::Set Availability for Team MEmber::::::");
            System.out.println("ORg name:: " + teamMemberDTO.getOrgName());
            System.out.println("meeting Window::" + teamMemberDTO.getMeetingWindow());
            System.out.println("timezone :: " + teamMemberDTO.getTimeZone());
            System.out.println("zoommeetingLink ::" + teamMemberDTO.getZoomMeetingLink());
            System.out.println("zoom meeting setup? ::" + teamMemberDTO.isZoomMeetingAvailable());
            System.out.println("onsitemeeting setup? :: " + teamMemberDTO.isOnSiteMeetingAvailable());
            System.out.println("::::Availability for Team MEmber::::::");
//
            //get authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                //organization information collection
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                //now get the user id
                Long userid = userPrincipal.getUserId();
                //Now save updated or new availability
                Boolean successSetup = teamMemberService.setTeamMemberAvailability(userid, teamMemberDTO, availabilityDTO);
                System.out.println("CONTROLLER RECEIVE SUCESSSETUP: "+ successSetup);
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
            System.out.println("UNSUCESSFUL SETUP");
            //return the availability form again with message
        }
        return "redirect:/availability_setup";
    }

    @GetMapping("/team-dashboard")
    public String getTeamMemberDashboard(){
        return "teammember_page";
    }
}
