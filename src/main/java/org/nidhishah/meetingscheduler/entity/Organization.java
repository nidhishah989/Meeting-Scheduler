package org.nidhishah.meetingscheduler.entity;

import jakarta.persistence.*;

@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "org_name")
    private String orgName;

    @Column(name = "org_description")
    private String orgDescription;

    @Column(name = "base_url")
    private String baseUrl;

//    @Column(name = "meeting_type")
//    private String meetingType;
//
//    @Column(name = "meeting_window")
//    private String meetingWindow;

    @Column(name="address_1")
    private String orgAddress1;

    @Column(name="address_2")
    private String orgAddress2;

    @Column(name="org_city")
    private String orgCity;
    @Column(name="org_state")
    private String orgState;

    @Column(name="org_country")
    private String orgCountry;

    @Column(name="org_contact")
    private String orgContact;

    public Organization() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgDescription() {
        return orgDescription;
    }

    public void setOrgDescription(String orgDescription) {
        this.orgDescription = orgDescription;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getOrgAddress1() {
        return orgAddress1;
    }

    public void setOrgAddress1(String orgAddress1) {
        this.orgAddress1 = orgAddress1;
    }

    public String getOrgAddress2() {
        return orgAddress2;
    }

    public void setOrgAddress2(String orgAddress2) {
        this.orgAddress2 = orgAddress2;
    }

    public String getOrgCity() {
        return orgCity;
    }

    public void setOrgCity(String orgCity) {
        this.orgCity = orgCity;
    }

    public String getOrgState() {
        return orgState;
    }

    public void setOrgState(String orgState) {
        this.orgState = orgState;
    }

    public String getOrgCountry() {
        return orgCountry;
    }

    public void setOrgCountry(String orgCountry) {
        this.orgCountry = orgCountry;
    }

    public String getOrgContact() {
        return orgContact;
    }

    public void setOrgContact(String orgContact) {
        this.orgContact = orgContact;
    }
}

