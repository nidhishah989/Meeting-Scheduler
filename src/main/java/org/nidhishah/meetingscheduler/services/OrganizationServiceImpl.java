package org.nidhishah.meetingscheduler.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService{

    private final OrganizationRepository organizationRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository,ModelMapper modelMapper) {
        this.organizationRepository = organizationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Organization setOrganization(String organization) {
        System.out.println("organization Setup for : " + organization);

        if (organizationRepository.findByOrgName(organization) != null) {
            return null; // Organization already exists // no setup is done, so it return null
        }
        else{
            // Map DTO to Entity
            Organization newOrg = new Organization();
            newOrg.setOrgName(organization);
            newOrg.setBaseUrl("/"+organization.toLowerCase());
            // Save the organization
            organizationRepository.save(newOrg);
            return organizationRepository.findByOrgName(newOrg.getOrgName());
//            return newOrg; // Organization setup successful
        }

    }

    @Override
    public OrganizationDTO findByOrgName(String organization) {
        Organization org = organizationRepository.findByOrgName(organization);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrganizationDTO organizationDTO = modelMapper.map(org,OrganizationDTO.class);
        return  organizationDTO;
    }
}
