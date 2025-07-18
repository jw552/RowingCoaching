package org.example.rowingcoaching.dto;

public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email
) {}