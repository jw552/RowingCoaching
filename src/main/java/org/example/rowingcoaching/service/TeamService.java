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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    /**
     * Create a new team with a coach
     */
    @Transactional
    public Team createTeam(String teamName, Long coachId) {
        // Get the coach
        User coach = userRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        // Create team with unique code
        Team team = new Team();
        team.setTeamName(teamName);
        team.setTeamCode(generateUniqueTeamCode());

        Team savedTeam = teamRepository.save(team);

        // Add coach to team
        UserTeam userTeam = new UserTeam(coach, savedTeam, UserTeam.Role.COACH);
        userTeamRepository.save(userTeam);

        return savedTeam;
    }

    /**
     * Create team without specifying coach upfront
     */
    public Team createTeam(Team team) {
        if (team.getTeamCode() == null || team.getTeamCode().isEmpty()) {
            team.setTeamCode(generateUniqueTeamCode());
        }
        return teamRepository.save(team);
    }

    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    public Optional<Team> getTeamByCode(String code) {
        return teamRepository.findByTeamCode(code);
    }

    /**
     * Get all members of a team
     */
    public List<User> getTeamMembers(Long teamId) {
        return userRepository.findActiveUsersByTeamId(teamId);
    }

    /**
     * Get members of a team with specific role
     */
    public List<User> getTeamMembersByRole(Long teamId, UserTeam.Role role) {
        return userRepository.findActiveUsersByTeamIdAndRole(teamId, role);
    }

    /**
     * Get coaches of a team
     */
    public List<User> getTeamCoaches(Long teamId) {
        return userRepository.findActiveUsersByTeamIdAndRole(teamId, UserTeam.Role.COACH);
    }

    /**
     * Get athletes of a team
     */
    public List<User> getTeamAthletes(Long teamId) {
        return userRepository.findActiveUsersByTeamIdAndRole(teamId, UserTeam.Role.ATHLETE);
    }

    /**
     * Get all UserTeam relationships for a team
     */
    public List<UserTeam> getTeamUserRelationships(Long teamId) {
        return userTeamRepository.findActiveByTeamId(teamId);
    }

    /**
     * Add a user to team with specified role
     */
    @Transactional
    public boolean addUserToTeam(Long teamId, Long userId, UserTeam.Role role) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (teamOpt.isPresent() && userOpt.isPresent()) {
            Team team = teamOpt.get();
            User user = userOpt.get();

            // Check if user is already in this team
            if (userTeamRepository.existsActiveByUserIdAndTeamId(userId, teamId)) {
                return false;
            }

            UserTeam userTeam = new UserTeam(user, team, role);
            userTeamRepository.save(userTeam);
            return true;
        }
        return false;
    }

    /**
     * Remove a user from team
     */
    @Transactional
    public boolean removeUserFromTeam(Long teamId, Long userId) {
        Optional<UserTeam> userTeamOpt = userTeamRepository.findActiveByUserIdAndTeamId(userId, teamId);

        if (userTeamOpt.isPresent()) {
            UserTeam userTeam = userTeamOpt.get();
            userTeam.setActive(false);
            userTeam.setLeftDate(java.time.LocalDateTime.now());
            userTeamRepository.save(userTeam);
            return true;
        }
        return false;
    }

    /**
     * Check if team has any active members
     */
    public boolean hasActiveMembers(Long teamId) {
        return teamRepository.hasActiveMembers(teamId);
    }

    /**
     * Delete team if it has no active members
     */
    @Transactional
    public boolean deleteTeamIfEmpty(Long teamId) {
        if (!hasActiveMembers(teamId)) {
            teamRepository.deleteById(teamId);
            return true;
        }
        return false;
    }

    /**
     * Generate unique team code
     */
    private String generateUniqueTeamCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (teamRepository.findByTeamCode(code).isPresent());
        return code;
    }

    /**
     * Get all teams
     */
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * Update team information
     */
    public Team updateTeam(Team team) {
        return teamRepository.save(team);
    }
}