package org.nidhishah.meetingscheduler.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;

/* Testing to save organization and retrieve it as application is doing.
*
* */
@SpringBootTest
public class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    private static Organization testOrganization;


    //set static organization first
    @BeforeAll
    static void setUp() {
        testOrganization = new Organization();
        testOrganization.setOrgName("Test Organization");
        testOrganization.setOrgDescription("Test Description");
        testOrganization.setOrgAddress1("123 Test St");
        testOrganization.setOrgAddress2("Suite 100");
        testOrganization.setOrgCity("Test City");
        testOrganization.setOrgState("Test State");
        testOrganization.setOrgCountry("Test Country");
        testOrganization.setOrgContact("1234567890");
    }

    //save organization and retrieve to check save method is working and findByOrgName working
    @Test
    public void testFindOrganizationByOrgName() {
        //save the organization
        organizationRepository.save(testOrganization);

        // Retrieve the organization by org name
        Organization foundOrganization = organizationRepository.findByOrgName("Test Organization").orElse(null);

        // Assert that the retrieved organization matches the expected organization
        assertEquals(testOrganization.getOrgName(), foundOrganization.getOrgName());
    }


}
