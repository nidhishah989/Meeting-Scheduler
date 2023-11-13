package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.*;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.nidhishah.meetingscheduler.services.ClientServiceImpl;
import org.nidhishah.meetingscheduler.services.TeamMemberServiceImpl;
import org.nidhishah.meetingscheduler.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ClientController {

    private UserServiceImpl userService;
    private ClientServiceImpl clientService;
    private TeamMemberServiceImpl teamMemberService;
    @Autowired
    public ClientController(UserServiceImpl userService, ClientServiceImpl clientService
                            ,TeamMemberServiceImpl teamMemberService) {
        this.userService = userService;
        this.clientService = clientService;
        this.teamMemberService = teamMemberService;
    }

    @PostMapping("/addclient")
    public String addNewClient(Model model, @ModelAttribute("newmember") NewOrgMemberDTO newOrgMemberDTO,
                                @RequestParam String roleName, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Inside the add client");
            System.out.println(newOrgMemberDTO.getEmail());
            System.out.println(newOrgMemberDTO.getFirstName());
            System.out.println(newOrgMemberDTO.getLastName());
            System.out.println(roleName); // roleName will be "client" here
            // get authenticated admin organization
            //check the client is already with organization or not. either as client, teammember or as admin
            // if is there, send error, this client is already exist with your organization
            // else: save this person and generate one time code and send email to the person and refresh the list in admin page
            //return the admin page.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                //organization information collection
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String adminOrganization = userPrincipal.getOrganizationName();
                //new member found
                if(userService.findUserByEmailAndOrganization(newOrgMemberDTO.getEmail(), adminOrganization)){
                    redirectAttributes.addFlashAttribute("clientAddError",newOrgMemberDTO.getEmail() + " is already member of "+ adminOrganization);
                    return "redirect:/adm_dashboard";
                }
                else{
                    //new member not found, so add to organization
                    clientService.registerNewClient(newOrgMemberDTO,adminOrganization);
                    redirectAttributes.addFlashAttribute("clientAddSuccess","New client added successfully and email sent.");
                    return "redirect:/adm_dashboard";

                }
                //Find that the new member is present in that organization or not

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return "file";
    }

    /**
     * This form only gets all team members first name and last name who are available for meetings..
     */

    @GetMapping("/meeting_schedule")
    public String getMeetingScheduleForm1(Model model){
        try{
            //get authenticated person info
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                //organization information collection
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String organizationName = userPrincipal.getOrganizationName();
                System.out.println("Client page get form1 controller");
                //call teammember service to get all teammembers list who are available for meetings
                List<TeamMemberDTO> availableTeamMembers= teamMemberService.getAvailableTeamMembersForMeetingSchedule(organizationName);
                System.out.println("Received "+availableTeamMembers.size() +" available from back for " + organizationName);
                model.addAttribute("teamembers",availableTeamMembers);
                model.addAttribute("organization",organizationName);
                return "client_page";
            }
            else{
                throw new Exception();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return "login";
        }
    }

    @PostMapping("/setup-meeting")
    public String getMeetingScheduleForm2(@ModelAttribute(name="selectedTeamMember")String id){
        //get id of the teammember and redirect to second form
        System.out.println("In the process of getting second schedule form, teammember id: "+ id);
        return "redirect:/schedule-meeting/"+id;
    }

    //get id of the teammember and return a page with list of meeting windows for each day..
    @GetMapping("/schedule-meeting/{id}")
    public String showSecondMeetingForm(@PathVariable(name="id")String id,Model model){
        try {
            DaysAvailabilityDTO availabilityDTO = new DaysAvailabilityDTO();
            System.out.println("IN THE SECOND FORM GET MAPPING: TEAMMEMBER:ID" + id);
            //get teammember info: firstname, last name, meeting type options, meeting window, timezone
            TeamMemberDTO teamMemberDTO = teamMemberService.getTeamMemberInfoById(id);
            //if user info found, and user is teammember or admin and user is enable
            if (teamMemberDTO != null) {
                System.out.println("IN THE SECOND FORM GET MAPPING: TEAMMEMBER FOUND");
                // have to check user organziation match with client org
                //get authenticated person info
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                    //organization information collection
                    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                    String organizationName = userPrincipal.getOrganizationName();
                    //check org matching - good to go, get availability
                    if (teamMemberDTO.getOrgName().equals(organizationName)) {
                        System.out.println("IN THE SECOND FORM GET MAPPING:TEAMMEMBER AND CLIENT ORGANIZATION MATCH");
                        //get days availability based on meeting window and team member id
                        availabilityDTO = teamMemberService.getTeamMemberMeetingAvail(teamMemberDTO.getMeetingWindow(),id);
                        if(availabilityDTO == null){
                            throw new Exception();
                        }
                    } else {
                        throw new Exception();
                    }
                }
            }
            else{
                throw new Exception();
            }
            System.out.println("IN THE SECOND FORM GET MAPPING: DATA HAS RECEIVED FROM BACK");
            //add everything in model and return the page
            model.addAttribute("teammember",teamMemberDTO);
            model.addAttribute("daysavail",availabilityDTO);
            return "meeting_schedule_page";
        }catch (Exception e){
            e.printStackTrace();
            return "Error/404";
        }

    }

    @PostMapping("/meetingschedule/{id}")
    public void setClientMeeting(@ModelAttribute(name="selectedDate") String meetingdate,
                                 @ModelAttribute(name="selectedMeetingType")String meetingType,
                                 @ModelAttribute(name="selectedTimeSlot") String meetingslot,
                                 @ModelAttribute(name="selectedDay") String meetingDay,
                                 @PathVariable(name="id")String teammemberId,
                                 RedirectAttributes redirectAttributes, TeamMemberDTO teamMemberDTO){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                //organization information collection
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String clientId = String.valueOf(userPrincipal.getUserId());
                String organization = userPrincipal.getOrganizationName();

                //Timezone model
                TimeSlot timeSlot = new TimeSlot();
                String[] times = meetingslot.split("-");
                timeSlot.setStartTime(times[0]);
                timeSlot.setEndTime(times[1]);
                //Meeting information stored.
                MeetingDTO meetingDTO = new MeetingDTO();
                meetingDTO.setMeetingType(meetingType);
                meetingDTO.setMeetingDate(meetingdate);
                meetingDTO.setMeetingDay(meetingDay);
                meetingDTO.setTimeslot(timeSlot);
                meetingDTO.setTeamMemberId(teammemberId);
                meetingDTO.setClientID(clientId);
                meetingDTO.setOrganization(organization);
                /////////////////////////////////////////////////////
                System.out.println("SCHEDULE MEETING FOR A CLIENT WITH TEAMMEMBER: ");
                System.out.println("Team Member id: " + teammemberId);
                System.out.println("Meeting Date: " + meetingdate);
                System.out.println("Meeting Day: " + meetingDay);
                System.out.println("Meeting Type: " + meetingType);
                System.out.println("Meeting Timeslot: " + meetingslot);
                System.out.println("FIrst name: "+ teamMemberDTO.getFirstName());
                System.out.println("Last name:" + teamMemberDTO.getLastName());
                ////////////////////////////////////////////////////////////////////
                // save the meeting and change the teammember availability
                //if success- change teammember availability
                if (clientService.saveClientMeeting(meetingDTO)) {
                    redirectAttributes.addFlashAttribute("success","Your Meeting has been scheduled with ");

                } else {
                    throw new Exception();
                }

            }
        }catch (Exception e){
            // return the meeting page again...
        }

    }
}
