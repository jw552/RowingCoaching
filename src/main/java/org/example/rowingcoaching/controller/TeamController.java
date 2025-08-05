package org.example.rowingcoaching.controller;

import org.example.rowingcoaching.model.Team;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.model.UserTeam;
import org.example.rowingcoaching.service.TeamService;
import org.example.rowingcoaching.service.UserService;
import org.example.rowingcoaching.dto.TeamDTO;
import org.example.rowingcoaching.dto.request.CreateTeamRequest;
import org.example.rowingcoaching.mapper.TeamMapper;
import org.example.rowingcoaching.dto.UserTeamDetailsDTO;
import org.example.rowingcoaching.mapper.UserTeamMapper;
import org.example.rowingcoaching.dto.UserDTO;
import org.example.rowingcoaching.mapper.UserMapper;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    /**
     * Create a team - creator is automatically assigned COACH role
     */
    @PostMapping
    @Transactional
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody CreateTeamRequest request, Authentication authentication) {
        String username = authentication.getName();
        System.out.println("[CREATE_TEAM] Attempting to create team. Authenticated username: " + username);
        User coach = userService.getUserByUsername(username)
                .orElse(null);
        if (coach == null) {
            System.out.println("[CREATE_TEAM] User not found in DB for username: " + username);
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        System.out.println("[CREATE_TEAM] User found: " + coach.getId() + " (" + coach.getUsername() + ")");

        // TeamService.createTeam automatically assigns creator as COACH
        Team savedTeam = teamService.createTeam(request.getName(), request.getLogo(), coach.getId());
        System.out.println("[CREATE_TEAM] Team created successfully: " + savedTeam.getTeamName() + " (ID: " + savedTeam.getId() + ")");
        return ResponseEntity.ok(TeamMapper.toDTO(savedTeam));
    }

    /**
     * Get team by ID
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long teamId) {
        return teamService.getTeamById(teamId)
                .map(team -> ResponseEntity.ok(TeamMapper.toDTO(team)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get team by code
     */
    @GetMapping("/code/{teamCode}")
    public ResponseEntity<TeamDTO> getTeamByCode(@PathVariable String teamCode) {
        return teamService.getTeamByCode(teamCode)
                .map(team -> ResponseEntity.ok(TeamMapper.toDTO(team)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all members of a team
     */
    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<UserDTO>> getTeamMembers(@PathVariable Long teamId) {
        List<User> members = teamService.getTeamMembers(teamId);
        List<UserDTO> memberDTOs = members.stream()
                .map(UserMapper::toDTO)
                .toList();
        return ResponseEntity.ok(memberDTOs);
    }

    /**
     * Get coaches of a team
     */
    @GetMapping("/{teamId}/coaches")
    public ResponseEntity<List<UserDTO>> getTeamCoaches(@PathVariable Long teamId) {
        List<User> coaches = teamService.getTeamCoaches(teamId);
        List<UserDTO> coachDTOs = coaches.stream()
                .map(UserMapper::toDTO)
                .toList();
        return ResponseEntity.ok(coachDTOs);
    }

    /**
     * Get athletes of a team
     */
    @GetMapping("/{teamId}/athletes")
    public ResponseEntity<List<UserDTO>> getTeamAthletes(@PathVariable Long teamId) {
        List<User> athletes = teamService.getTeamAthletes(teamId);
        List<UserDTO> athleteDTOs = athletes.stream()
                .map(UserMapper::toDTO)
                .toList();
        return ResponseEntity.ok(athleteDTOs);
    }

    /**
     * Get all team relationships (includes roles, join dates, etc.)
     */
    @GetMapping("/{teamId}/relationships")
    public ResponseEntity<List<UserTeamDetailsDTO>> getTeamUserRelationships(@PathVariable Long teamId) {
        List<UserTeam> relationships = teamService.getTeamUserRelationships(teamId);
        List<UserTeamDetailsDTO> relationshipDTOs = relationships.stream()
                .map(UserTeamMapper::toDetailsDTO)
                .toList();
        return ResponseEntity.ok(relationshipDTOs);
    }

    /**
     * Add a user to team (coach only) - allows coach to specify role
     */
    @PostMapping("/{teamId}/members")
    public ResponseEntity<String> addUserToTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId,
            @RequestParam UserTeam.Role role,
            Authentication authentication) {

        // Verify the current user is a coach of this team
        String username = authentication.getName();
        User currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!userService.hasRoleInTeam(currentUser.getId(), teamId, UserTeam.Role.COACH)) {
            return ResponseEntity.status(403).body("Only coaches can add members to the team");
        }

        boolean success = teamService.addUserToTeam(teamId, userId, role);
        if (!success) {
            return ResponseEntity.badRequest().body("Unable to add user to team");
        }
        return ResponseEntity.ok("User added to team successfully");
    }

    /**
     * Remove a user from team (coach only)
     */
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<String> removeUserFromTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            Authentication authentication) {

        // Verify the current user is a coach of this team
        String username = authentication.getName();
        User currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!userService.hasRoleInTeam(currentUser.getId(), teamId, UserTeam.Role.COACH)) {
            return ResponseEntity.status(403).body("Only coaches can remove members from the team");
        }

        boolean success = teamService.removeUserFromTeam(teamId, userId);
        if (!success) {
            return ResponseEntity.badRequest().body("Unable to remove user from team");
        }
        return ResponseEntity.ok("User removed from team successfully");
    }

    /**
     * Update user's role in team (coach only)
     */
    @PutMapping("/{teamId}/members/{userId}/role")
    public ResponseEntity<String> updateUserRoleInTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @RequestParam UserTeam.Role newRole,
            Authentication authentication) {

        // Verify the current user is a coach of this team
        String username = authentication.getName();
        User currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!userService.hasRoleInTeam(currentUser.getId(), teamId, UserTeam.Role.COACH)) {
            return ResponseEntity.status(403).body("Only coaches can update member roles");
        }

        boolean success = userService.updateUserRoleInTeam(userId, teamId, newRole);
        if (!success) {
            return ResponseEntity.badRequest().body("Unable to update user role");
        }
        return ResponseEntity.ok("User role updated successfully");
    }

    /**
     * Join team by code - automatically assigns ATHLETE role
     */
    @PostMapping("/join/{teamCode}")
    public ResponseEntity<String> joinTeamByCode(
            @PathVariable String teamCode,
            Authentication authentication) {

        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // UserService.addUserToTeam automatically assigns ATHLETE role
        boolean success = userService.addUserToTeam(user.getId(), teamCode);
        if (!success) {
            return ResponseEntity.badRequest().body("Unable to join team. Invalid team code or already a member.");
        }
        return ResponseEntity.ok("Successfully joined team as ATHLETE");
    }

    /**
     * Get all teams
     */
    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        List<TeamDTO> teamDTOs = teams.stream()
                .map(TeamMapper::toDTO)
                .toList();
        return ResponseEntity.ok(teamDTOs);
    }

    /**
     * Update team information (coach only)
     */
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamDTO> updateTeam(
            @PathVariable Long teamId,
            @Valid @RequestBody TeamDTO teamDTO,
            Authentication authentication) {

        // Verify the current user is a coach of this team
        String username = authentication.getName();
        User currentUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!userService.hasRoleInTeam(currentUser.getId(), teamId, UserTeam.Role.COACH)) {
            return ResponseEntity.status(403).body(null);
        }

        Optional<Team> existingTeam = teamService.getTeamById(teamId);
        if (existingTeam.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Team team = existingTeam.get();
        team.setTeamName(teamDTO.teamName());

        Team updatedTeam = teamService.updateTeam(team);
        return ResponseEntity.ok(TeamMapper.toDTO(updatedTeam));
    }
}