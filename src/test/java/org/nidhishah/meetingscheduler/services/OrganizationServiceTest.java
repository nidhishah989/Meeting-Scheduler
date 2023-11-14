/******
 * OrganizationSerivce testing
 * organization is saved- findbyOrgname-> give organization information
 * Update origination -> success-> from service or unsuccessful from service
 */
package org.nidhishah.meetingscheduler.services;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrganizationServiceTest {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    OrganizationService organizationService;

    private static OrganizationDTO staticOrganization;

    @Autowired
    ModelMapper modelMapper;

    @Test
    @Order(1)
    void setUpOrganization() {
        staticOrganization = new OrganizationDTO();
        staticOrganization.setOrgName("testOrg");
        Organization organization = modelMapper.map(staticOrganization,Organization.class);
        //save Org
        organizationRepository.save(organization);

    }

    @Test
    @Order(2)
    void testFindByOrgName() {
        //get org
        OrganizationDTO foundOrganization = organizationService.findByOrgName("testOrg");

        // check org
        assertEquals("testOrg", foundOrganization.getOrgName());
    }

    //positive test
    @Test
    @Order(3)
    void testSetOrganizationDetail() {
        //Provide extra org information
        staticOrganization.setOrgDescription("Testing org");
        staticOrganization.setOrgAddress1("address1");
        staticOrganization.setOrgAddress2("address2");
        staticOrganization.setOrgState("state");
        staticOrganization.setOrgCountry("country");

        // Update the organization details
        boolean isUpdated = organizationService.setOrganizationDetail(staticOrganization);
        assertTrue(isUpdated, "Organization details updated.");
    }

    //negative test
    @Test
    @Order(4)
    void testSetOrganizationDetailReturnFalse(){
        OrganizationDTO organizationDTO = new OrganizationDTO();
        //Provide extra org information
        organizationDTO.setOrgName("test");
        organizationDTO.setOrgDescription("Testing org");
        organizationDTO.setOrgAddress1("address1");
        organizationDTO.setOrgAddress2("address2");
        organizationDTO.setOrgState("state");
        organizationDTO.setOrgCountry("country");
        // Update the organization details
        boolean isUpdated1 = organizationService.setOrganizationDetail(organizationDTO);
        System.out.println(isUpdated1);
        Assertions.assertFalse(isUpdated1, "Unsuccessful in updates.");
    }
}
