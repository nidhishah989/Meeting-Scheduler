package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.ClientDTO;
import org.nidhishah.meetingscheduler.dto.MeetingDTO;
import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;

import java.util.List;

public interface ClientService {

    public List<ClientDTO> getClientListByOrgName(String organization);

    public void registerNewClient(NewOrgMemberDTO newOrgMemberDTO, String organization) throws Exception;

    public boolean saveClientMeeting(MeetingDTO meetingDTO);
}
