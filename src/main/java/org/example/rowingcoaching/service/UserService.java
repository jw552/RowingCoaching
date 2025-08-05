package org.example.rowingcoaching.service;

import lombok.RequiredArgsConstructor;
import org.example.rowingcoaching.model.Team;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.model.UserTeam;
import org.example.rowingcoaching.repository.TeamRepository;
import org.example.rowingcoaching.repository.UserRepository;
import org.example.rowingcoaching.repository.UserTeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Add user to team - automatically assigns ATHLETE role for joining existing teams
     * (Role parameter is ignored - users joining existing teams are always ATHLETE)
     */
    @Transactional
    public boolean addUserToTeam(Long userId, String teamCode, UserTeam.Role role) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Team> teamOpt = teamRepository.findByTeamCode(teamCode);

        if (userOpt.isPresent() && teamOpt.isPresent()) {
            User user = userOpt.get();
            Team team = teamOpt.get();

            // Check if user is already in this team
            if (userTeamRepository.existsActiveByUserIdAndTeamId(userId, team.getId())) {
                return false; // User already in team
            }

            // Always assign ATHLETE role when joining existing teams
            // (Team creators are handled separately in TeamService.createTeam)
            UserTeam userTeam = new UserTeam(user, team, UserTeam.Role.ATHLETE);
            userTeamRepository.save(userTeam);
            return true;
        }
        return false;
    }

    /**
     * Add user to team with teamCode - automatically assigns ATHLETE role
     */
    @Transactional
    public boolean addUserToTeam(Long userId, String teamCode) {
        return addUserToTeam(userId, teamCode, UserTeam.Role.ATHLETE);
    }

    /**
     * Remove user from team (soft delete)
     */
    @Transactional
    public boolean removeUserFromTeam(Long userId, Long teamId) {
        Optional<UserTeam> userTeamOpt = userTeamRepository.findActiveByUserIdAndTeamId(userId, teamId);

        if (userTeamOpt.isPresent()) {
            UserTeam userTeam = userTeamOpt.get();
            userTeam.setActive(false);
            userTeam.setLeftDate(LocalDateTime.now());
            userTeamRepository.save(userTeam);
            return true;
        }
        return false;
    }

    /**
     * Get all teams for a user
     */
    public List<Team> getUserTeams(Long userId) {
        return teamRepository.findActiveTeamsByUserId(userId);
    }

    /**
     * Get teams where user has a specific role
     */
    public List<Team> getUserTeamsByRole(Long userId, UserTeam.Role role) {
        return teamRepository.findActiveTeamsByUserIdAndRole(userId, role);
    }

    /**
     * Get user's role in a specific team
     */
    public Optional<UserTeam.Role> getUserRoleInTeam(Long userId, Long teamId) {
        return userTeamRepository.findActiveByUserIdAndTeamId(userId, teamId)
                .map(UserTeam::getRole);
    }

    /**
     * Check if user has specific role in team
     */
    public boolean hasRoleInTeam(Long userId, Long teamId, UserTeam.Role role) {
        return userTeamRepository.existsActiveByUserIdAndTeamIdAndRole(userId, teamId, role);
    }

    /**
     * Get all UserTeam relationships for a user
     */
    public List<UserTeam> getUserTeamRelationships(Long userId) {
        return userTeamRepository.findActiveByUserId(userId);
    }

    /**
     * Update user's role in a team
     */
    @Transactional
    public boolean updateUserRoleInTeam(Long userId, Long teamId, UserTeam.Role newRole) {
        Optional<UserTeam> userTeamOpt = userTeamRepository.findActiveByUserIdAndTeamId(userId, teamId);

        if (userTeamOpt.isPresent()) {
            UserTeam userTeam = userTeamOpt.get();
            userTeam.setRole(newRole);
            userTeamRepository.save(userTeam);
            return true;
        }
        return false;
    }

    /**
     * Check if user is a coach of any team
     */
    public boolean isUserCoachOfAnyTeam(Long userId) {
        return userTeamRepository.existsActiveByUserIdAndRole(userId, UserTeam.Role.COACH);
    }
}