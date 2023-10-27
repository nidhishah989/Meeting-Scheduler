package org.nidhishah.meetingscheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;


@Entity
@Table(name = "organizations")
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

    @Column(name = "meeting_type")
    private String meetingType;

    @Column(name = "meeting_window")
    private String meetingWindow;

    // Constructors, getters, setters, and other fields as needed

    public Organization() {
    }

    public Long getId() {
        return id;
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

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getMeetingWindow() {
        return meetingWindow;
    }

    public void setMeetingWindow(String meetingWindow) {
        this.meetingWindow = meetingWindow;
    }


}

