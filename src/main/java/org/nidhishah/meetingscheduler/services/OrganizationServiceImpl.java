package org.nidhishah.meetingscheduler.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

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
    public Organization setOrganization(OrganizationDTO organizationDTO) {
        System.out.println("organization Setup for : " + organizationDTO.getOrgName());

        if (organizationRepository.findByOrgName(organizationDTO.getOrgName()) != null) {
            return null; // Organization already exists // no setup is done, so it return null
        }
        else{
            // Map DTO to Entity
            organizationDTO.setBaseUrl("/"+organizationDTO.getOrgName());
            Organization newOrg = modelMapper.map(organizationDTO,Organization.class);

//            newOrg.setBaseUrl("/"+organizationDTO.get.toLowerCase());
            // Save the organization
            organizationRepository.save(newOrg);
            return newOrg;
//            return organizationRepository.findByOrgName(newOrg.getOrgName());
//            return newOrg; // Organization setup successful
        }

    }

    @Override
    public OrganizationDTO findByOrgName(String organization) {
        Optional<Organization> orgOptional = organizationRepository.findByOrgName(organization);
        if(orgOptional.isPresent()){
            Organization org = orgOptional.get();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            OrganizationDTO organizationDTO = modelMapper.map(org,OrganizationDTO.class);
            return  organizationDTO;
        }
        else{
            return null;
        }
    }
}
