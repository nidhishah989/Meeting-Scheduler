/*******
 * Organization Service:
 * update organization detail
 * find organization is there or not by organization name(unique)
 * By Nidhi Shah
 */
package org.nidhishah.meetingscheduler.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //Organization is already saved-> update it's information and return true else false
    @Override
    public boolean setOrganizationDetail(OrganizationDTO organizationDTO) {
        String organizationname= organizationDTO.getOrgName();
        System.out.println("organization Setup for : " + organizationname);
        //if org is there, update it and return true otherwise return false
        Optional<Organization> orgOptional = organizationRepository.findByOrgName(organizationname);
        if(orgOptional.isPresent()){
            //update organization, return true
            Organization oldorg = orgOptional.get();
            oldorg.setOrgDescription(organizationDTO.getOrgDescription());
            oldorg.setOrgAddress1(organizationDTO.getOrgAddress1());
            oldorg.setOrgAddress2(organizationDTO.getOrgAddress2());
            oldorg.setOrgCity(organizationDTO.getOrgCity());
            oldorg.setOrgState(organizationDTO.getOrgState());
            oldorg.setOrgCountry(organizationDTO.getOrgCountry());
            oldorg.setOrgContact(organizationDTO.getOrgContact());

            organizationRepository.save(oldorg);
            return true;
        }
        else{
            return false;
        }

    }


    // find organization by organizatio name - found -> return else return null
    @Override
    public OrganizationDTO findByOrgName(String organization) {
        organization = organization.toLowerCase();
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
