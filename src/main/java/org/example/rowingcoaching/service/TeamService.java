package org.example.rowingcoaching.service;

import lombok.RequiredArgsConstructor;
import org.example.rowingcoaching.model.Team;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.repository.TeamRepository;
import org.example.rowingcoaching.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public Team createTeam(Team team) {
        String teamCode = UUID.randomUUID().toString().substring(0, 8);
        team.setTeamCode(teamCode);
        return teamRepository.save(team);
    }

    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    public Optional<Team> getTeamByCode(String code) {
        return teamRepository.findByTeamCode(code);
    }

    public List<User> getTeamMembers(Long teamId) {
        return userRepository.findAll().stream()
                .filter(u -> u.getTeam() != null && u.getTeam().getId().equals(teamId))
                .toList();
    }
}