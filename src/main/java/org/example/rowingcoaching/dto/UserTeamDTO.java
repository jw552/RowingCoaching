package org.example.rowingcoaching.dto;

import java.time.LocalDateTime;

public record UserTeamDTO(
        Long id,
        Long userId,
        Long teamId,
        String role,
        LocalDateTime joinedDate,
        LocalDateTime leftDate,
        Boolean active
) {}