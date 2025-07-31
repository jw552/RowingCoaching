package org.example.rowingcoaching.dto;

import java.time.LocalDate;
import java.util.List;

public record WorkoutDTO(
        Long id,
        Long userId,
        LocalDate date,
        int totalDistance,
        int totalDuration,
        double averagePace,
        int averageStrokeRate,
        String rawJsonData,
        List<WorkoutSegmentDTO> segments
) {}
