package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.ClientDTO;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private UserRepository userRepository;

    @Autowired
    public ClientServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<ClientDTO> getClientListByOrgName(String organization) {

        List<ClientDTO> clientDTOList= userRepository.getUsersByOrgNameAndRole(organization);
        if(!clientDTOList.isEmpty()){
            return clientDTOList;
        }
        return null;
    }
}
