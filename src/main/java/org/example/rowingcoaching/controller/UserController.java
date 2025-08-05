package org.example.rowingcoaching.controller;

import org.example.rowingcoaching.model.Team;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.model.UserTeam;
import org.example.rowingcoaching.service.UserService;
import org.example.rowingcoaching.dto.UserDTO;
import org.example.rowingcoaching.dto.request.CreateUserRequest;
import org.example.rowingcoaching.mapper.UserMapper;
import org.example.rowingcoaching.dto.TeamDTO;
import org.example.rowingcoaching.mapper.TeamMapper;
import org.example.rowingcoaching.dto.UserTeamDetailsDTO;
import org.example.rowingcoaching.mapper.UserTeamMapper;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(UserMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = UserMapper.fromCreateRequest(request);
        User saved = userService.save(user);
        return ResponseEntity.ok(UserMapper.toDTO(saved));
    }

    /**
     * Get all teams for the current user
     */
    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getCurrentUserTeams(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Team> teams = userService.getUserTeams(user.getId());
        List<TeamDTO> teamDTOs = teams.stream()
                .map(TeamMapper::toDTO)
                .toList();

        return ResponseEntity.ok(teamDTOs);
    }

    /**
     * Get teams where current user is a coach
     */
    @GetMapping("/teams/coaching")
    public ResponseEntity<List<TeamDTO>> getCurrentUserCoachingTeams(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Team> teams = userService.getUserTeamsByRole(user.getId(), UserTeam.Role.COACH);
        List<TeamDTO> teamDTOs = teams.stream()
                .map(TeamMapper::toDTO)
                .toList();

        return ResponseEntity.ok(teamDTOs);
    }

    /**
     * Get teams where current user is an athlete
     */
    @GetMapping("/teams/playing")
    public ResponseEntity<List<TeamDTO>> getCurrentUserPlayingTeams(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Team> teams = userService.getUserTeamsByRole(user.getId(), UserTeam.Role.ATHLETE);
        List<TeamDTO> teamDTOs = teams.stream()
                .map(TeamMapper::toDTO)
                .toList();

        return ResponseEntity.ok(teamDTOs);
    }

    /**
     * Get user's role in a specific team
     */
    @GetMapping("/teams/{teamId}/role")
    public ResponseEntity<UserTeam.Role> getUserRoleInTeam(@PathVariable Long teamId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<UserTeam.Role> role = userService.getUserRoleInTeam(user.getId(), teamId);
        return role.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Join a team as athlete - automatically assigns ATHLETE role
     */
    @PostMapping("/teams/join")
    public ResponseEntity<String> joinTeam(@RequestParam String teamCode, Authentication authentication) {
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
     * Leave a team
     */
    @PostMapping("/teams/{teamId}/leave")
    public ResponseEntity<String> leaveTeam(@PathVariable Long teamId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean success = userService.removeUserFromTeam(user.getId(), teamId);
        if (!success) {
            return ResponseEntity.badRequest().body("Unable to leave team. You are not a member of this team.");
        }
        return ResponseEntity.ok("Successfully left team");
    }

    /**
     * Get all team relationships for current user (includes role, join date, etc.)
     */
    @GetMapping("/team-relationships")
    public ResponseEntity<List<UserTeamDetailsDTO>> getCurrentUserTeamRelationships(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserTeam> relationships = userService.getUserTeamRelationships(user.getId());
        List<UserTeamDetailsDTO> relationshipDTOs = relationships.stream()
                .map(UserTeamMapper::toDetailsDTO)
                .toList();
        return ResponseEntity.ok(relationshipDTOs);
    }
}