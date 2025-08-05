package org.example.rowingcoaching.dto;

import java.time.LocalDateTime;

public record WorkoutResultDTO(
        Long id,
        String workoutType,
        LocalDateTime completedAt,
        Integer totalDistance,
        Integer totalTime,
        Double averagePace,
        Double averageSplit,
        Integer calories,
        Integer averageStrokeRate
) {} 