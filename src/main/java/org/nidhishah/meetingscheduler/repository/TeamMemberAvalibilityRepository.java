package org.nidhishah.meetingscheduler.repository;

import org.nidhishah.meetingscheduler.entity.TeamMemberAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberAvalibilityRepository extends JpaRepository<TeamMemberAvailability,Long> {

    public TeamMemberAvailability getTeamMemberAvailabilityByTeammember_Id(Long teammemberid);

}
