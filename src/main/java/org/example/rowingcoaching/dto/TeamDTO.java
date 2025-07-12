package org.example.rowingcoaching.dto;

public record TeamDTO(
        Long id,
        String name,
        String teamCode,
        Long coachId
) {}
