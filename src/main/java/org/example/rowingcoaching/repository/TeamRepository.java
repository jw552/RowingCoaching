package org.example.rowingcoaching.repository;

import org.example.rowingcoaching.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByTeamCode(String teamCode);

    // Find teams by user
    @Query("SELECT ut.team FROM UserTeam ut WHERE ut.user.id = :userId AND ut.active = true")
    List<Team> findActiveTeamsByUserId(@Param("userId") Long userId);

    // Find teams where user has a specific role
    @Query("SELECT ut.team FROM UserTeam ut WHERE ut.user.id = :userId AND ut.role = :role AND ut.active = true")
    List<Team> findActiveTeamsByUserIdAndRole(@Param("userId") Long userId, @Param("role") org.example.rowingcoaching.model.UserTeam.Role role);

    // Find teams by username
    @Query("SELECT ut.team FROM UserTeam ut WHERE ut.user.username = :username AND ut.active = true")
    List<Team> findActiveTeamsByUsername(@Param("username") String username);

    // Check if team exists and is not empty
    @Query("SELECT COUNT(ut) > 0 FROM UserTeam ut WHERE ut.team.id = :teamId AND ut.active = true")
    boolean hasActiveMembers(@Param("teamId") Long teamId);
}