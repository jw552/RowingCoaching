package org.example.rowingcoaching.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rowingcoaching.dto.request.CreateUserRequest;
import org.example.rowingcoaching.dto.request.LoginRequest;
import org.example.rowingcoaching.dto.response.AuthResponse;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.repository.UserRepository;
import org.example.rowingcoaching.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtProvider;

    public User register(@Valid CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already in use.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use.");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));


        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword())
        );
        System.out.println("[LOGIN] Attempting login with: " + request.getUsernameOrEmail());

        User user = userRepository.findByUsernameOrEmail(
                request.getUsernameOrEmail(), request.getUsernameOrEmail()
        ).orElseThrow(() -> new RuntimeException("User not found."));

        String token = jwtProvider.generateToken(user.getUsername());
        return new AuthResponse(token, user);

    }
}
