package org.nidhishah.meetingscheduler.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.dto.UserDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.User;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
//import org.nidhishah.meetingscheduler.repository.TeamMemberRepository;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.nidhishah.meetingscheduler.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final BCryptPasswordEncoder encoder;



    @Autowired
    public TeamMemberServiceImpl(OrganizationService organizationService, RoleService roleService,
                                 ModelMapper modelMapper, UserRepository userRepository,
                                 OrganizationRepository organizationRepository,BCryptPasswordEncoder encoder) {
        this.organizationService = organizationService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.userRepository=userRepository;
        this.organizationRepository = organizationRepository;
        this.encoder =encoder;
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
}
