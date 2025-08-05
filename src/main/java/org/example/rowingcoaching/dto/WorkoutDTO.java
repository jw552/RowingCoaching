package org.example.rowingcoaching.dto;

import org.example.rowingcoaching.model.Workout;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record WorkoutDTO(
        Long id,
        Workout.WorkoutType type,
        Map<String, Object> metrics,
        Workout.WorkoutStatus status,
        LocalDateTime createdAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        
        // Results from completed workout
        Integer totalDistance,
        Integer totalTime,
        Double averagePace,
        Double averageSplit,
        Integer calories,
        Integer averageStrokeRate,
        
        // User relationships
        Long athleteId,
        String athleteName,
        Long coachId,
        String coachName,
        
        List<WorkoutSegmentDTO> segments
) {}
