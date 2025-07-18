package org.example.rowingcoaching.repository;

import org.example.rowingcoaching.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Find users by team
    @Query("SELECT ut.user FROM UserTeam ut WHERE ut.team.id = :teamId AND ut.active = true")
    List<User> findActiveUsersByTeamId(@Param("teamId") Long teamId);

    // Find users by team and role
    @Query("SELECT ut.user FROM UserTeam ut WHERE ut.team.id = :teamId AND ut.role = :role AND ut.active = true")
    List<User> findActiveUsersByTeamIdAndRole(@Param("teamId") Long teamId, @Param("role") org.example.rowingcoaching.model.UserTeam.Role role);

    // Find users by team code
    @Query("SELECT ut.user FROM UserTeam ut WHERE ut.team.teamCode = :teamCode AND ut.active = true")
    List<User> findActiveUsersByTeamCode(@Param("teamCode") String teamCode);
}