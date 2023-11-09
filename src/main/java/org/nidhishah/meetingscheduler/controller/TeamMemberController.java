package org.nidhishah.meetingscheduler.controller;

import org.nidhishah.meetingscheduler.dto.DaysAvailabilityDTO;
import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
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

@Controller
public class TeamMemberController {

    private UserServiceImpl userService;
    private TeamMemberServiceImpl teamMemberService;
    @Autowired
    public TeamMemberController(UserServiceImpl userService, TeamMemberServiceImpl teamMemberService) {
        this.userService = userService;
        this.teamMemberService = teamMemberService;
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
        model.addAttribute("daysavail",new DaysAvailabilityDTO());
        return "availability_setup_page.html";
    }
}
