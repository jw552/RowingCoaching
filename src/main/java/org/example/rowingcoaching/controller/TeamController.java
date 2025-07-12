package org.example.rowingcoaching.controller;

import org.example.rowingcoaching.model.Team;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.service.TeamService;
import org.example.rowingcoaching.service.UserService;
import org.example.rowingcoaching.dto.TeamDTO;
import org.example.rowingcoaching.dto.request.CreateTeamRequest;
import org.example.rowingcoaching.mapper.TeamMapper;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Create a team (coach only)
    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        User coach = userService.getUserById(request.getCoachId())
                .orElseThrow(() -> new RuntimeException("Coach not found"));
        Team team = TeamMapper.fromCreateRequest(request, coach);
        Team saved = teamService.createTeam(team);
        return ResponseEntity.ok(TeamMapper.toDTO(saved));
    }

    // Join a team by code (athlete only)
    @PostMapping("/join")
    public ResponseEntity<String> joinTeam(@RequestParam Long userId, @RequestParam String teamCode) {
        boolean success = userService.assignUserToTeam(userId, teamCode);
        if (!success) {
            return ResponseEntity.badRequest().body("Invalid user or team code");
        }
        return ResponseEntity.ok("Joined team successfully");
    }

    @GetMapping("/{teamId}/members")
    public List<User> getTeamMembers(@PathVariable Long teamId) {
        return userService.getAllUsers().stream()
                .filter(u -> u.getTeam() != null && u.getTeam().getId().equals(teamId))
                .toList();
    }

    @GetMapping("/{teamId}")
    public Optional<Team> getTeamById(@PathVariable Long teamId) {
        return teamService.getTeamById(teamId);
    }
}