package org.nidhishah.meetingscheduler.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.nidhishah.meetingscheduler.dto.*;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.TeamMemberAvailability;
import org.nidhishah.meetingscheduler.entity.User;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
//import org.nidhishah.meetingscheduler.repository.TeamMemberRepository;
import org.nidhishah.meetingscheduler.repository.TeamMemberAvalibilityRepository;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.nidhishah.meetingscheduler.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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



    @Autowired
    public TeamMemberServiceImpl(OrganizationService organizationService, RoleService roleService,
                                 ModelMapper modelMapper, UserRepository userRepository,
                                 OrganizationRepository organizationRepository,@Lazy BCryptPasswordEncoder encoder,
                                 TeamMemberAvalibilityRepository teamMemberAvalibilityRepository) {
        this.organizationService = organizationService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.userRepository=userRepository;
        this.organizationRepository = organizationRepository;
        this.encoder =encoder;
        this.teamMemberAvalibilityRepository = teamMemberAvalibilityRepository;
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
        TeamMemberAvailability teamMemberAvailability = teamMemberAvalibilityRepository.getByTeammember_Id(user.getId());
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
}
