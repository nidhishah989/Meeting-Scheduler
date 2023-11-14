/*************
 * Get list of clients
 * set new client
 * set client meeting
 * *************/
package org.nidhishah.meetingscheduler.services;

import org.modelmapper.ModelMapper;
import org.nidhishah.meetingscheduler.dto.ClientDTO;
import org.nidhishah.meetingscheduler.dto.MeetingDTO;
import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
import org.nidhishah.meetingscheduler.entity.Meeting;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.User;
import org.nidhishah.meetingscheduler.repository.MeetingRepository;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.nidhishah.meetingscheduler.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private UserRepository userRepository;
    private OrganizationRepository organizationRepository;

    private RoleServiceImpl roleService;

    private final ModelMapper modelMapper;

    private MeetingRepository meetingRepository;

    @Autowired
    public ClientServiceImpl(UserRepository userRepository,
                             OrganizationRepository organizationRepository,
                             RoleServiceImpl roleService, ModelMapper modelMapper,
                             MeetingRepository meetingRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.meetingRepository = meetingRepository;

    }

    /*
    * *************************** Get List of Clients for a given organization
    * */
    @Override
    public List<ClientDTO> getClientListByOrgName(String organization) {

        List<ClientDTO> clientDTOList= userRepository.getUsersByOrgNameAndRole(organization);
        if(!clientDTOList.isEmpty()){
            return clientDTOList;
        }
        return null;
    }

    /*
     * *************************** Add new Client to given organization
     * */

    @Override
    public void registerNewClient(NewOrgMemberDTO newOrgMemberDTO, String organization) throws Exception {
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
                System.out.println("Role found for client: id: "+role.getId());
                // new member user - set role and organization
                User newclient = modelMapper.map(newOrgMemberDTO,User.class);
                newclient.setRole(role);
                newclient.setOrganization(orgdetail);
                // set one time passcode
                // Generate a one-time code
                String oneTimeCode = CodeGenerator.generateSixDigitCode();
                newclient.setTempPasscode(oneTimeCode);
                //set is not active yet
                newclient.setEnabled(false);
                //save new member
                userRepository.save(newclient);
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

    /*********** Meeting is set for client *********/
    @Override
    public boolean saveClientMeeting(MeetingDTO meetingDTO) {
        try{
            //get client from user
            Optional<User> clientOptional = userRepository.findById(Long.valueOf(meetingDTO.getClientID()));
            User client = clientOptional.get();
            //get teammember from user
            Optional<User> teamMemberOptional = userRepository.findById(Long.valueOf(meetingDTO.getClientID()));
            User teammember = teamMemberOptional.get();
            // get organization from org name
            Optional<Organization> organizationOptinal = organizationRepository.findByOrgName(meetingDTO.getOrganization());
            Organization organization = organizationOptinal.get();
            // save those in the meeting entity
            Meeting meeting = new Meeting();
            meeting.setClient(client);
            meeting.setProvider(teammember);
            meeting.setOrganization(organization);
            meeting.setMeetingDate(meetingDTO.getMeetingDate());
            meeting.setMeetingType(meetingDTO.getMeetingType());
            meeting.setMeetingStartTime(meetingDTO.getTimeslot().getStartTime());
            meeting.setMeetingEndTIme(meetingDTO.getTimeslot().getEndTime());
            meetingRepository.save(meeting);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
