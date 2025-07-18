package org.example.rowingcoaching.repository;

import org.example.rowingcoaching.model.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {

    // Find active relationship by user and team
    @Query("SELECT ut FROM UserTeam ut WHERE ut.user.id = :userId AND ut.team.id = :teamId AND ut.active = true")
    Optional<UserTeam> findActiveByUserIdAndTeamId(@Param("userId") Long userId, @Param("teamId") Long teamId);

    // Find active relationship by user and team code
    @Query("SELECT ut FROM UserTeam ut WHERE ut.user.id = :userId AND ut.team.teamCode = :teamCode AND ut.active = true")
    Optional<UserTeam> findActiveByUserIdAndTeamCode(@Param("userId") Long userId, @Param("teamCode") String teamCode);

    // Find all active relationships for a user
    @Query("SELECT ut FROM UserTeam ut WHERE ut.user.id = :userId AND ut.active = true")
    List<UserTeam> findActiveByUserId(@Param("userId") Long userId);

    // Find all active relationships for a team
    @Query("SELECT ut FROM UserTeam ut WHERE ut.team.id = :teamId AND ut.active = true")
    List<UserTeam> findActiveByTeamId(@Param("teamId") Long teamId);

    // Find active relationships by user and role
    @Query("SELECT ut FROM UserTeam ut WHERE ut.user.id = :userId AND ut.role = :role AND ut.active = true")
    List<UserTeam> findActiveByUserIdAndRole(@Param("userId") Long userId, @Param("role") UserTeam.Role role);

    // Find active relationships by team and role
    @Query("SELECT ut FROM UserTeam ut WHERE ut.team.id = :teamId AND ut.role = :role AND ut.active = true")
    List<UserTeam> findActiveByTeamIdAndRole(@Param("teamId") Long teamId, @Param("role") UserTeam.Role role);

    // Check if user is already in team (active)
    @Query("SELECT COUNT(ut) > 0 FROM UserTeam ut WHERE ut.user.id = :userId AND ut.team.id = :teamId AND ut.active = true")
    boolean existsActiveByUserIdAndTeamId(@Param("userId") Long userId, @Param("teamId") Long teamId);

    // Check if user has specific role in team
    @Query("SELECT COUNT(ut) > 0 FROM UserTeam ut WHERE ut.user.id = :userId AND ut.team.id = :teamId AND ut.role = :role AND ut.active = true")
    boolean existsActiveByUserIdAndTeamIdAndRole(@Param("userId") Long userId, @Param("teamId") Long teamId, @Param("role") UserTeam.Role role);

    // Find coaches for a team
    @Query("SELECT ut FROM UserTeam ut WHERE ut.team.id = :teamId AND ut.role = 'COACH' AND ut.active = true")
    List<UserTeam> findActiveCoachesByTeamId(@Param("teamId") Long teamId);

    // Find athletes for a team
    @Query("SELECT ut FROM UserTeam ut WHERE ut.team.id = :teamId AND ut.role = 'ATHLETE' AND ut.active = true")
    List<UserTeam> findActiveAthletesByTeamId(@Param("teamId") Long teamId);
}