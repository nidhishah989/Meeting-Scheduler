package org.nidhishah.meetingscheduler.entity;

import jakarta.persistence.*;

@Entity
public class Meeting {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String MeetingType;

    private String MeetingStartTime;

    private String MeetingEndTIme;

    private String MeetingDate;

    @ManyToOne(targetEntity = ClientExtraInfo.class, fetch = FetchType.LAZY)
    private ClientExtraInfo Client;

    @ManyToOne(targetEntity = TeamMemberExtraInfo.class, fetch = FetchType.LAZY)
    private TeamMemberExtraInfo provider;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    private Organization organization;
}
