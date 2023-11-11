package org.nidhishah.meetingscheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeamMemberExtraInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;
    private String meetingWindow;
    private boolean zoomMeetingAvailable;
    private boolean onSiteMeetingAvailable;
    private String zoomMeetingLink;
    private String timeZone;


}
