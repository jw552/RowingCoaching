package org.example.rowingcoaching.controller;

import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.service.UserService;
import org.example.rowingcoaching.dto.UserDTO;
import org.example.rowingcoaching.dto.request.CreateUserRequest;
import org.example.rowingcoaching.mapper.UserMapper;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = UserMapper.fromCreateRequest(request);
        User saved = userService.save(user);
        return ResponseEntity.ok(UserMapper.toDTO(saved));
    }
}