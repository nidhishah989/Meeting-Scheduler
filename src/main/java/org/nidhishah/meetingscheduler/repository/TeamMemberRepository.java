package org.nidhishah.meetingscheduler.repository;

import org.nidhishah.meetingscheduler.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository <TeamMember, Long> {

}
