package org.nidhishah.meetingscheduler.entity;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class ClientExtraInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    private User user;

}
