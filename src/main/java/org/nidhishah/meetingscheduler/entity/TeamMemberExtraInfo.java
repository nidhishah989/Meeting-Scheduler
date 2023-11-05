package org.nidhishah.meetingscheduler.entity;

import jakarta.persistence.*;
@Entity
public class TeamMemberExtraInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;
    @Column(name = "meeting_type")
    private String meetingType;

    @Column(name = "meeting_window")
    private String meetingWindow;
}
