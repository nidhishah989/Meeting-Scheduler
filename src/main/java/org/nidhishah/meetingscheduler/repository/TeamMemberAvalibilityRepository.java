package org.nidhishah.meetingscheduler.repository;

import org.nidhishah.meetingscheduler.entity.TeamMemberAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberAvalibilityRepository extends JpaRepository<TeamMemberAvailability,Long> {

    public TeamMemberAvailability getTeamMemberAvailabilityByTeammember_Id(Long teammemberid);

}
