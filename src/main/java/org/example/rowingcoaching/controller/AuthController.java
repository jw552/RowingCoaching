package org.example.rowingcoaching.controller;

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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
}
