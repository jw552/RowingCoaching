package org.example.rowingcoaching.dto;

import java.time.LocalDate;

public record WorkoutDTO(
        Long id,
        Long userId,
        LocalDate date,
        int distance,
        int strokeRate,
        double pace,
        String rawJsonData
) {}
