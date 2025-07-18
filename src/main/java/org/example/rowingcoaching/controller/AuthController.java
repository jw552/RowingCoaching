package org.example.rowingcoaching.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.rowingcoaching.dto.request.LoginRequest;
import org.example.rowingcoaching.dto.request.CreateUserRequest;
import org.example.rowingcoaching.dto.response.AuthResponse;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.service.AuthService;
import org.example.rowingcoaching.dto.UserDTO;
import org.example.rowingcoaching.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.example.rowingcoaching.security.JwtTokenProvider;
import org.example.rowingcoaching.repository.UserRepository;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtProvider;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody CreateUserRequest request) {
        User user = authService.register(request);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        try {
            // Get token from header
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new AuthResponse(null, null, "Authorization header missing or invalid")
                );
            }

            String token = authHeader.substring(7);

            // Extract the username from token
            String username = jwtProvider.getUsernameFromToken(token);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new AuthResponse(null, null, "Username not found in token")
                );
            }

            // Get fresh user data
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate new token with current user data
            String newToken = jwtProvider.generateToken(username);
            UserDTO userDTO = UserMapper.toDTO(user);

            return ResponseEntity.ok(new AuthResponse(newToken, userDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponse(null, null, e.getMessage())
            );
        }
    }
}