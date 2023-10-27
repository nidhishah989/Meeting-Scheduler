package org.nidhishah.meetingscheduler.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.RoleDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.TeamMember;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import org.nidhishah.meetingscheduler.repository.RoleRepository;
import org.nidhishah.meetingscheduler.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    private final OrganizationService organizationService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final TeamMemberRepository teamMemberRepository;

    private final OrganizationRepository organizationRepository;



    @Autowired
    public TeamMemberServiceImpl(OrganizationService organizationService, RoleService roleService,
                                 ModelMapper modelMapper, TeamMemberRepository teamMemberRepository,
                                 OrganizationRepository organizationRepository) {
        this.organizationService = organizationService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.teamMemberRepository=teamMemberRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public void registerAdmin(TeamMemberDTO teamMemberDTO, OrganizationDTO organizationDTO) throws Exception {
        // Call organization service for init org setup
        if( organizationService.findByOrgName(organizationDTO.getOrgName()) ==null){
//          //set orgdto as organization should be for init org setup
            System.out.println("ORGDTO: "+organizationDTO.getOrgName());
            organizationDTO.setBaseUrl("/"+organizationDTO.getOrgName());
            System.out.println("ORGDTO" +organizationDTO.getBaseUrl());
            //get organization
            Organization organization = modelMapper.map(organizationDTO,Organization.class);
            System.out.println("ORG: "+organization.getOrgName());
            System.out.println("ORG :"+organization.getBaseUrl());
            //get admin role
            Role role = roleService.findByRoleName("admin");
            //map teammember
            TeamMember adminmember = modelMapper.map(teamMemberDTO,TeamMember.class);
            organizationRepository.save(organization);
            adminmember.setRole(role);
            adminmember.setOrganization(organization);
            teamMemberRepository.save(adminmember);

        }
        else{
            System.out.println("Organization is already in system.. might need to SingIn");
            throw new Exception();
        }

    }
}
