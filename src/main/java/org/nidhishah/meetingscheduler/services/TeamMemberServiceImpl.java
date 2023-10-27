package org.nidhishah.meetingscheduler.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.RoleDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.TeamMember;
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



    @Autowired
    public TeamMemberServiceImpl(OrganizationService organizationService, RoleService roleService,
                                 ModelMapper modelMapper, TeamMemberRepository teamMemberRepository) {
        this.organizationService = organizationService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.teamMemberRepository=teamMemberRepository;
    }

    @Override
    public void registerAdmin(TeamMemberDTO teamMemberDTO) {
        // Call organization service for init org setup
        Organization returnedorg = organizationService.setOrganization(teamMemberDTO.getOrganization().getOrgName());
        // setadmin if setorganization is true
        if( returnedorg !=null){
            System.out.println("Returned Org"+returnedorg.getId());
            System.out.println(teamMemberDTO.getOrganization().getOrgName());
            System.out.println(teamMemberDTO.getEmail());
            System.out.println(teamMemberDTO.getFirstName());
            System.out.println(teamMemberDTO.getLastName());
            System.out.println(teamMemberDTO.getPassword());
            // Convert teammember DTO to teammember
            TeamMember teamMember = modelMapper.map(teamMemberDTO, TeamMember.class);
            System.out.println("Before"+teamMember.getOrganization().getId());
            System.out.println("Before baseurl "+teamMember.getOrganization().getBaseUrl());
            teamMemberDTO.setOrganization(modelMapper.map(returnedorg,OrganizationDTO.class));
            TeamMember teamMember1 = modelMapper.map(teamMemberDTO,TeamMember.class);
            System.out.println("After"+teamMember1.getOrganization().getId());
            System.out.println("After baseurl "+teamMember1.getOrganization().getBaseUrl());
            teamMember1.setOrganization(returnedorg);
            System.out.println("After direct org setup"+teamMember1.getOrganization().getId());
            System.out.println("After baseurl "+teamMember1.getOrganization().getBaseUrl());

        }

        else{
            System.out.println("Organization is already in system.. might need to SingIn");
        }

        //setup admin

    }
}
