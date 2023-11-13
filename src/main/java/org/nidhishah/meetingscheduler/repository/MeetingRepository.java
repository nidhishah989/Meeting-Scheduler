package org.nidhishah.meetingscheduler.repository;

import org.nidhishah.meetingscheduler.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository <Meeting,Long> {

}
