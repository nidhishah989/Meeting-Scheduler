package org.nidhishah.meetingscheduler.repository;

import org.nidhishah.meetingscheduler.entity.TeamMemberExtraInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TeamMemberExtraInfoRepository extends JpaRepository<TeamMemberExtraInfo, Long> {

    public TeamMemberExtraInfo getTeamMemberExtraInfoByUser_Id(Long id);
}
