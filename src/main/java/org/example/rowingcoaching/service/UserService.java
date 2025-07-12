package org.example.rowingcoaching.service;



import lombok.RequiredArgsConstructor;
import org.example.rowingcoaching.model.Team;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.repository.TeamRepository;
import org.example.rowingcoaching.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

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

    public boolean assignUserToTeam(Long userId, String teamCode) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Team> teamOpt = teamRepository.findByTeamCode(teamCode);

        if (userOpt.isPresent() && teamOpt.isPresent()) {
            User user = userOpt.get();
            user.setTeam(teamOpt.get());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
