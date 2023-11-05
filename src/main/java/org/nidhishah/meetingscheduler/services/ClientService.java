package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.ClientDTO;

import java.util.List;

public interface ClientService {

    public List<ClientDTO> getClientListByOrgName(String organization);
}
