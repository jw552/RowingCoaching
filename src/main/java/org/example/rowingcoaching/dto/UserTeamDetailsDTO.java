package org.example.rowingcoaching.dto;

import java.time.LocalDateTime;

// This DTO includes full user and team information for convenient display
public record UserTeamDetailsDTO(
        Long id,
        UserDTO user,
        TeamDTO team,
        String role,
        LocalDateTime joinedDate,
        LocalDateTime leftDate,
        Boolean active
) {}