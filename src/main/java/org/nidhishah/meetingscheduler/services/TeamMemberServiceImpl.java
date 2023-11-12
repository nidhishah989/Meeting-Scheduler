package org.nidhishah.meetingscheduler.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.nidhishah.meetingscheduler.dto.*;
import org.nidhishah.meetingscheduler.entity.*;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
//import org.nidhishah.meetingscheduler.repository.TeamMemberRepository;
import org.nidhishah.meetingscheduler.repository.TeamMemberAvalibilityRepository;
import org.nidhishah.meetingscheduler.repository.TeamMemberExtraInfoRepository;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.nidhishah.meetingscheduler.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import java.time.LocalTime;
import java.util.*;

import org.modelmapper.TypeMap;

@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    private final OrganizationService organizationService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private BCryptPasswordEncoder encoder;

    private TeamMemberAvalibilityRepository teamMemberAvalibilityRepository;
    private TeamMemberExtraInfoRepository teamMemberExtraInfoRepository;



    @Autowired
    public TeamMemberServiceImpl(OrganizationService organizationService, RoleService roleService,
                                 ModelMapper modelMapper, UserRepository userRepository,
                                 OrganizationRepository organizationRepository,@Lazy BCryptPasswordEncoder encoder,
                                 TeamMemberAvalibilityRepository teamMemberAvalibilityRepository,
                                 TeamMemberExtraInfoRepository teamMemberExtraInfoRepository) {
        this.organizationService = organizationService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.userRepository=userRepository;
        this.organizationRepository = organizationRepository;
        this.encoder =encoder;
        this.teamMemberAvalibilityRepository = teamMemberAvalibilityRepository;
        this.teamMemberExtraInfoRepository= teamMemberExtraInfoRepository;
    }

    @Override
    public void registerAdmin(UserDTO userDTO, OrganizationDTO organizationDTO) throws Exception {
        // Call organization service for init org setup
        organizationDTO.setOrgName(organizationDTO.getOrgName().toLowerCase());
        if( organizationService.findByOrgName(organizationDTO.getOrgName()) ==null){
//          //set orgdto as organization should be for init org setup
            System.out.println("ORGDTO: "+organizationDTO.getOrgName());
            organizationDTO.setBaseUrl("/"+organizationDTO.getOrgName());
            System.out.println("ORGDTO" +organizationDTO.getBaseUrl());
            organizationDTO.setOrgName(organizationDTO.getOrgName().toLowerCase());
            //get organization
            Organization organization = modelMapper.map(organizationDTO,Organization.class);
            System.out.println("ORG: "+organization.getOrgName());
            System.out.println("ORG :"+organization.getBaseUrl());
            //get admin role
            Role role = roleService.findByRoleName("admin");
            //set userDTO username:
            userDTO.setUsername(userDTO.getEmail().split("@")[0]);
            //map teammember
            User adminmember = modelMapper.map(userDTO, User.class);
            //encrypt password
            adminmember.setPassword(encoder.encode(userDTO.getPassword()));
            organizationRepository.save(organization);
            adminmember.setRole(role);
            adminmember.setOrganization(organization);
            adminmember.setEnabled(true);
            userRepository.save(adminmember);

        }
        else{
            System.out.println("Organization is already in system.. might need to SingIn");
            throw new Exception();
        }

    }

    @Override
    public List<TeamMemberDTO> getTeamMembersByOrganization(String organization) {
        // get teammembers list of objects from database
        List<Object[]> teamMemberObjects = userRepository.findUsersByOrganizationNameAndRole(organization);
        //map it to teammemberDTO
        List<TeamMemberDTO> teamMemberList = new ArrayList<>();

        // Define a custom mapping configuration
        modelMapper.typeMap(Object[].class, TeamMemberDTO.class)
                .addMappings(mapping -> {
                    mapping.map(src -> (String)src[0], TeamMemberDTO::setFirstName);
                    mapping.map(src -> (String)src[1], TeamMemberDTO::setLastName);
                    mapping.map(src -> (String)src[2], TeamMemberDTO::setRoleName);
                    mapping.map(src -> (String)src[3], TeamMemberDTO::setOrgName);
                });

        if(!teamMemberObjects.isEmpty()) {
            for (Object[] memberObject : teamMemberObjects) {
                System.out.println(Arrays.toString(memberObject));
                System.out.println(memberObject[0]);

                TeamMemberDTO teamMember = modelMapper.map(memberObject, TeamMemberDTO.class);
                System.out.println("$$$$$$$$$$$$$$$$$$$$: "+ teamMember);
                teamMemberList.add(teamMember);
            }
            return teamMemberList;
        }
        else {
            System.out.println("Object is Empty????????????????????/");
            return null;
        }
    }

    @Override
    public List<TeamMemberDTO> getTeamMembersByOrgName(String organization) {
        // get from repository:
        List<TeamMemberDTO> teamMemberDTOList = userRepository.getUsersByOrganizationNameAndRole(organization);
        for( TeamMemberDTO member: teamMemberDTOList){
            System.out.println("TEam member: "+ member.toString());
        }

        return teamMemberDTOList;
    }

    @Override
    public void registerNewTeamMember(NewOrgMemberDTO newOrgMemberDTO, String organization) throws Exception{

        //get organization
        try{
            System.out.println("............IN registerNEwTeamMember.......................");
            System.out.println("Admin Org: "+organization);
            Optional<Organization> orgOptional = organizationRepository.findByOrgName(organization);
            if(orgOptional.isPresent()){
                // add new Team Member
                Organization orgdetail = orgOptional.get();
                System.out.println("After org found, org id:"+orgdetail.getId());
                //get role
                Role role = roleService.findByRoleName(newOrgMemberDTO.getRoleName());
                System.out.println("Role found for teammember: id: "+role.getId());
                // new member user - set role and organization
                User newMember = modelMapper.map(newOrgMemberDTO,User.class);
                newMember.setRole(role);
                newMember.setOrganization(orgdetail);
                // set one time passcode
                // Generate a one-time code
                String oneTimeCode = CodeGenerator.generateSixDigitCode();
                newMember.setTempPasscode(oneTimeCode);
                //set is not active yet
                newMember.setEnabled(false);
                //save new member
                userRepository.save(newMember);
                //send email to user

            }
            else{
                System.out.println("Organization not found during adding new member");
                throw new Exception("Organization not found.");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Organization not found. Exception");
        }
    }

    /*
     * ********** If sign up person is not admin-> complete Sign Up process
     * *********** return rolename or empty string
     * */
    @Override
    public String completeUserSignUpProcess(SignUPDTO signUPDTO) {
        //check the user
        User user = userRepository.findUserByEmailAndOrganization(signUPDTO.getEmail(), signUPDTO.getOrganization());
        System.out.println("SignUP Procsess: found user: "+ user.getOrganization().getOrgName() );
        // user role is not null and not "admin"
        if (user.getRole() != null && !"admin".equals(user.getRole().getRoleName())) {
            System.out.println("SignUP Procsess: user role: "+ user.getRole().getRoleName());
            //if user is not enabled, and password is empty: good to go for completing sign up
            System.out.println("SignUP Procsess: user enable: "+ user.getisEnabled() );
//            System.out.println("SignUP Procsess: user password Empty?: "+ user.getPassword().isEmpty() );
            if (!user.getisEnabled()) {

                System.out.println("SignUP Procsess: user temp passcode from database: "+ user.getTempPasscode());
                System.out.println("SignUP Procsess: user input passcode: "+ signUPDTO.getTempPasccode() );
                //check given passcode is matching with user database passcode
                if (signUPDTO.getTempPasccode().equals(user.getTempPasscode())) {
                    // complete sign up
                    user.setUsername(signUPDTO.getEmail().split("@")[0].toLowerCase());
                    user.setEnabled(true);
                    user.setPassword(encoder.encode(signUPDTO.getPassword()));
                    //remove temp code, as user is active now
                    user.setTempPasscode("");
                    userRepository.save(user);
                    System.out.println("After user is saved, "+user.getRole().getRoleName());
                    // return user rolename
                    return user.getRole().getRoleName();
                }
            }
        }
        return "";
    }

    ///////////////////// TEAM MEMBER AVAILABILITY SETUP FOR SETAVAILABILITY FORM /////
    /*  If teammeber entry(check by id) in the availability - fetch availability
    *    If no entry: create default timeslot of 9:00 am to 17:00(5:00 PM) for each day
    *    at the end return the DTO
     */
    @Override
    public DaysAvailabilityDTO getTeamMemberAvailability(String username, String organization) {
        //empty dto
        DaysAvailabilityDTO availabilityDTO = new DaysAvailabilityDTO();
        // find teammember id from user by username and orgname
        User user = userRepository.findByUsernameAndOrganizationOrgName(username,organization);
        //Do user have availability setup?
        TeamMemberAvailability teamMemberAvailability = teamMemberAvalibilityRepository.getTeamMemberAvailabilityByTeammember_Id(user.getId());
        // Not Empty- Let's create dto with stored values - deserialize each day if present
        if (teamMemberAvailability!= null)
        {
            availabilityDTO.setMondayTimeSlot(deserializeDayTimeSlots(teamMemberAvailability.getMondayTimeSlot()));
            availabilityDTO.setTuesdayTimeSlot(deserializeDayTimeSlots(teamMemberAvailability.getTuesdayTimeSlot()));
            availabilityDTO.setWednesdayTimeSlot(deserializeDayTimeSlots(teamMemberAvailability.getWednesdayTimeSlot()));
            availabilityDTO.setThursdayTimeSlot(deserializeDayTimeSlots(teamMemberAvailability.getThursdayTimeSlot()));
            availabilityDTO.setFridayTimeSlot(deserializeDayTimeSlots(teamMemberAvailability.getFridayTimeSlot()));
            availabilityDTO.setSaturdayTimeSlot(deserializeDayTimeSlots(teamMemberAvailability.getSaturdayTimeSlot()));
            availabilityDTO.setSundayTimeSlot(deserializeDayTimeSlots(teamMemberAvailability.getSundayTimeSlot()));
        }
        // Empty- load default one
        else
        {
            //create default timeslot
            TimeSlot timeSlot = new TimeSlot(LocalTime.of(9,0),LocalTime.of(17,0));
            List<TimeSlot> defaulttimeSlotList = new ArrayList<>();
            defaulttimeSlotList.add(timeSlot);
            availabilityDTO.setMondayTimeSlot(defaulttimeSlotList);
            availabilityDTO.setTuesdayTimeSlot(defaulttimeSlotList);
            availabilityDTO.setWednesdayTimeSlot(defaulttimeSlotList);
            availabilityDTO.setThursdayTimeSlot(defaulttimeSlotList);
            availabilityDTO.setFridayTimeSlot(defaulttimeSlotList);
            availabilityDTO.setSaturdayTimeSlot(defaulttimeSlotList);
            availabilityDTO.setSundayTimeSlot(defaulttimeSlotList);
        }
        return availabilityDTO;
    }

    /// Save or Update Team Member Availability
    /****************************
    * Check userid present in availability table
     * If yes, get that, and update days availability based on user's input
     * If no, create new, add all days availability based on user's input
     * Save the availability
     * return true-> if success, either return false
    * **********************************/
    @Override
    public boolean setTeamMemberAvailability(Long id, TeamMemberDTO teamMemberDTO, DaysAvailabilityDTO daysAvailabilityDTO) {
        try{
            System.out.println("IN MEETING AVAILABILITY SETUP");
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            System.out.println("CurrentUserID: "+ id);
            // first let's get user from user
            User currentuser= userRepository.getUsersById(id);
            System.out.println("found user by id, username: "+ currentuser.getUsername());
            //let's check teammemberextrainfo present or not
            TeamMemberExtraInfo memberExtraInfo = teamMemberExtraInfoRepository.getTeamMemberExtraInfoByUser_Id(id);
            //update it
            if(memberExtraInfo !=null){
                System.out.println("Teammember extra info need to update:");
                memberExtraInfo.setMeetingWindow(teamMemberDTO.getMeetingWindow());
                memberExtraInfo.setTimeZone(teamMemberDTO.getTimeZone());
                memberExtraInfo.setOnSiteMeetingAvailable(teamMemberDTO.isOnSiteMeetingAvailable());
                memberExtraInfo.setZoomMeetingAvailable(teamMemberDTO.isZoomMeetingAvailable());
                memberExtraInfo.setZoomMeetingLink(teamMemberDTO.getZoomMeetingLink());
                teamMemberExtraInfoRepository.save(memberExtraInfo);
                System.out.println("Teammember extra info updated:");
            }
            //make new entry
            else{
                System.out.println("Teammember extra info need to setup:");
                TeamMemberExtraInfo teamMemberExtraInfo = modelMapper.map(teamMemberDTO,TeamMemberExtraInfo.class);
                teamMemberExtraInfo.setUser(currentuser);
//                teamMemberExtraInfo.setMeetingWindow(teamMemberDTO.getMeetingWindow());
//                teamMemberExtraInfo.setTimeZone(teamMemberDTO.getTimeZone());
//                teamMemberExtraInfo.setOnSiteMeetingAvailable(teamMemberDTO.isOnSiteMeetingAvailable());
//                teamMemberExtraInfo.setZoomMeetingAvailable(teamMemberDTO.isZoomMeetingAvailable());
//                teamMemberExtraInfo.setZoomMeetingLink(teamMemberDTO.getZoomMeetingLink());
                teamMemberExtraInfoRepository.save(teamMemberExtraInfo);
                System.out.println("Teammember extra info setup done:");
            }
            // let's check user availability present or not
            TeamMemberAvailability memberAvailability = teamMemberAvalibilityRepository.getTeamMemberAvailabilityByTeammember_Id(id);
            //the availability have already seted up
            if(memberAvailability!= null){
                System.out.println("member availability need to update:");
                //first serialize list and get string for each day
                memberAvailability.setMondayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getMondayTimeSlot()));
                memberAvailability.setTuesdayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getTuesdayTimeSlot()));
                memberAvailability.setWednesdayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getWednesdayTimeSlot()));
                memberAvailability.setThursdayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getThursdayTimeSlot()));
                memberAvailability.setFridayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getFridayTimeSlot()));
                memberAvailability.setSaturdayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getSaturdayTimeSlot()));
                memberAvailability.setSundayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getSundayTimeSlot()));
                teamMemberAvalibilityRepository.save(memberAvailability);
                System.out.println("member availability updated:");
            }
            //the availability has not set up yet
            else{
                System.out.println("member availability set up new needed:");
                    TeamMemberAvailability teamMemberAvailability = new TeamMemberAvailability();
                    teamMemberAvailability.setTeammember(currentuser);
                    teamMemberAvailability.setMondayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getMondayTimeSlot()));
                    teamMemberAvailability.setTuesdayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getTuesdayTimeSlot()));
                    teamMemberAvailability.setWednesdayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getWednesdayTimeSlot()));
                    teamMemberAvailability.setThursdayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getThursdayTimeSlot()));
                    teamMemberAvailability.setFridayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getFridayTimeSlot()));
                    teamMemberAvailability.setSaturdayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getSaturdayTimeSlot()));
                    teamMemberAvailability.setSundayTimeSlot(serializeDayTimeSlots(daysAvailabilityDTO.getSundayTimeSlot()));
                    teamMemberAvalibilityRepository.save(teamMemberAvailability);
                System.out.println("member availability set up new done");
            }
            System.out.println("$$$$$$ User Availability Setup successfully done.");
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("$$$$$$ ERROR: User Availability Setup unsuccessful");
        return false;
    }

    /*****************************
    * This service will provide all available team members from given organization
     * isenable =true, and entry in availability table
    * */
    @Override
    public List<TeamMemberDTO> getAvailableTeamMembersForMeetingSchedule(String organization) {
        List<Object[]> teamMembersList =userRepository.findUsersByOrganizationNameAndRole(organization);
        System.out.println(organization + " have total "+teamMembersList.size() + " Team Members.");
        //now add only those team member if is enable and if enable-
        // and have atleast one meeting type available and availability present
        List<TeamMemberDTO> availableMemberList = new ArrayList<>();
        Iterator<Object[]> iterator = teamMembersList.iterator();
        while (iterator.hasNext()) {
            Object[] memberObject = iterator.next();
            boolean isMemberEnabled = (boolean) memberObject[memberObject.length - 1];
            System.out.println(memberObject[0] + " " + memberObject[1] + " is enable? "+ isMemberEnabled);
            //member is enable
            if (isMemberEnabled) {
                //now get the id and try to find - any meeting type is on?
                Long memberid = (Long) memberObject[memberObject.length -2];
                System.out.println(memberObject[0] + " " + memberObject[1] + " ID: ? "+ memberid);
                //get meetingtype from user's id
                TeamMemberExtraInfo memberExtraInfo = teamMemberExtraInfoRepository.getTeamMemberExtraInfoByUser_Id(memberid);
                if(memberExtraInfo.isOnSiteMeetingAvailable() || memberExtraInfo.isZoomMeetingAvailable()){
                    //get teammemberavailability entry
                    TeamMemberAvailability teamMemberAvailability = teamMemberAvalibilityRepository.getTeamMemberAvailabilityByTeammember_Id(memberid);
                    Boolean isMemberAvailabilityPresent = (teamMemberAvailability != null) ? true : false;
                    System.out.println(memberObject[0] + " " + memberObject[1] + " has availability setup? "+ isMemberAvailabilityPresent);
                    if(isMemberAvailabilityPresent){
                        //good to go, add member firstname and lastname and id to teammemberDTO
                        TeamMemberDTO availablemember = new TeamMemberDTO();
                        availablemember.setFirstName((String) memberObject[0]);
                        availablemember.setLastName((String) memberObject[1]);
                        availablemember.setId(memberid);
                        availableMemberList.add(availablemember);
                        System.out.println(Arrays.toString(availableMemberList.stream().toArray()));

                    }
                }

            }
        }

        return availableMemberList;
    }

    //just gather team member infor: first name, last name, organization,
    // meeting type list, meeting window, timezone
    //user is already in database, however, if is not teammember or admin
    // or is not enable yet or user with such id not found
    @Override
    public TeamMemberDTO getTeamMemberInfoById(String Id) {
        TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
        Long id = Long.parseLong(Id);
        //get user info from user repository first
        User teammember = userRepository.getUsersById(id);
        if (teammember != null && teammember.getisEnabled() &&
                (teammember.getRole().getRoleName().equals("admin") || teammember.getRole().getRoleName().equals("teammember"))) {
        // transfer data to teammemberDTO
            teamMemberDTO.setId(id);
            teamMemberDTO.setFirstName(teammember.getFirstName());
            teamMemberDTO.setLastName(teammember.getLastName());
            teamMemberDTO.setOrgName(teammember.getOrganization().getOrgName());
            teamMemberDTO.setRoleName(teammember.getRole().getRoleName());

            // Get user extra information from userextrainforrepository
            TeamMemberExtraInfo teamMemberExtraInfo = teamMemberExtraInfoRepository.getTeamMemberExtraInfoByUser_Id(id);
            //transfer data to teammemberDTO
            teamMemberDTO.setMeetingWindow(teamMemberExtraInfo.getMeetingWindow());
            teamMemberDTO.setTimeZone(teamMemberExtraInfo.getTimeZone());
            teamMemberDTO.setOnSiteMeetingAvailable(teamMemberExtraInfo.isOnSiteMeetingAvailable());
            teamMemberDTO.setZoomMeetingAvailable(teamMemberExtraInfo.isZoomMeetingAvailable());
            return teamMemberDTO;
        }
        else{
            return null;
        }
    }

    //teammember information is already checked befor this call,
    //Convert each availability in the form of meeting window gap and return the availability DTO
    @Override
    public DaysAvailabilityDTO getTeamMemberMeetingAvail(String meetingWindow, String id) {
        Long teamid = Long.parseLong(id);
        Integer meetwindow = Integer.parseInt(meetingWindow);
        DaysAvailabilityDTO daysAvailabilityDTO = new DaysAvailabilityDTO();
        //let's get actual availability from back
        TeamMemberAvailability memberAvailability = teamMemberAvalibilityRepository.getTeamMemberAvailabilityByTeammember_Id(teamid);
        if(memberAvailability != null){
           // first need to deserialize string to list- pass this list to get meeting window timeslot list
            // only if the string is not null.
            //for monday
            if(memberAvailability.getMondayTimeSlot()!=null){
                List<TimeSlot> dayTimeSlots = deserializeDayTimeSlots(memberAvailability.getMondayTimeSlot());
                daysAvailabilityDTO.setMondayTimeSlot(prepareMeetingTimeslotsToShow(meetwindow,dayTimeSlots));
            }
            //for tuesday
            if(memberAvailability.getTuesdayTimeSlot()!=null){
                List<TimeSlot> dayTimeSlots = deserializeDayTimeSlots(memberAvailability.getTuesdayTimeSlot());
                daysAvailabilityDTO.setTuesdayTimeSlot(prepareMeetingTimeslotsToShow(meetwindow,dayTimeSlots));
            }
            //for wednesday
            if(memberAvailability.getWednesdayTimeSlot()!=null){
                List<TimeSlot> dayTimeSlots = deserializeDayTimeSlots(memberAvailability.getWednesdayTimeSlot());
                daysAvailabilityDTO.setWednesdayTimeSlot(prepareMeetingTimeslotsToShow(meetwindow,dayTimeSlots));
            }
            //for thursday
            if(memberAvailability.getThursdayTimeSlot()!=null){
                List<TimeSlot> dayTimeSlots = deserializeDayTimeSlots(memberAvailability.getThursdayTimeSlot());
                daysAvailabilityDTO.setThursdayTimeSlot(prepareMeetingTimeslotsToShow(meetwindow,dayTimeSlots));
            }
            //for friday
            if(memberAvailability.getFridayTimeSlot()!=null){
                List<TimeSlot> dayTimeSlots = deserializeDayTimeSlots(memberAvailability.getFridayTimeSlot());
                daysAvailabilityDTO.setFridayTimeSlot(prepareMeetingTimeslotsToShow(meetwindow,dayTimeSlots));
            }
            //for saturday
            if(memberAvailability.getSaturdayTimeSlot()!=null){
                List<TimeSlot> dayTimeSlots = deserializeDayTimeSlots(memberAvailability.getSundayTimeSlot());
                daysAvailabilityDTO.setSaturdayTimeSlot(prepareMeetingTimeslotsToShow(meetwindow,dayTimeSlots));
            }
            //for sunday
            if(memberAvailability.getSundayTimeSlot()!=null){
                List<TimeSlot> dayTimeSlots = deserializeDayTimeSlots(memberAvailability.getSundayTimeSlot());
                daysAvailabilityDTO.setSundayTimeSlot(prepareMeetingTimeslotsToShow(meetwindow,dayTimeSlots));
            }

           return daysAvailabilityDTO;
        }
        else{
            return null;
        }
    }

    private List<TimeSlot> deserializeDayTimeSlots(String dayTimeSlots){
        TeamMemberAvailability teamavailibility = new TeamMemberAvailability();
        //if deserialize throw exception, just put default one
        try {
            return teamavailibility.deserializeTimeSlots(dayTimeSlots);
        }catch (Exception e){
            TimeSlot defaultslot = new TimeSlot(LocalTime.of(9,0),LocalTime.of(17,0));
            List<TimeSlot> defaultSlotList = new ArrayList<>();
            defaultSlotList.add(defaultslot);
            return defaultSlotList;
        }
    }

    private String serializeDayTimeSlots(List<TimeSlot> timeSlotList){
        TeamMemberAvailability teamavailibility = new TeamMemberAvailability();
        //if serialize throw exception,just put null
        try {
            return teamavailibility.serializeTimeSlots(timeSlotList);
        }catch (Exception e){
            System.out.println("serialize get wrong.");
            return null;
        }
    }

    private List<TimeSlot> prepareMeetingTimeslotsToShow(Integer window,List<TimeSlot> dayTimeSlotList){
        List<TimeSlot> meetingSlots = new ArrayList<>();
        for (TimeSlot slot : dayTimeSlotList) {
            LocalTime start = LocalTime.parse(slot.getStartTime());
            LocalTime end = LocalTime.parse(slot.getEndTime());

            while (start.isBefore(end)) {
                LocalTime nextTime = start.plusMinutes(window);
                meetingSlots.add(new TimeSlot(start, nextTime));
                start = nextTime;
            }
        }
        return meetingSlots;
    }


}
