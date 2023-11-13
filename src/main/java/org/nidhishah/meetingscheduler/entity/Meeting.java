package org.nidhishah.meetingscheduler.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String MeetingType;

    private String MeetingStartTime;

    private String MeetingEndTIme;

    private String MeetingDate;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User client;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User provider;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    private Organization organization;
}
