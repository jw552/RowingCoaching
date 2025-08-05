package org.example.rowingcoaching.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteWorkoutRequest {
    private WorkoutResult result;

    @Getter
    @Setter
    public static class WorkoutResult {
        private Integer totalDistance; // in meters
        private Integer totalTime; // in seconds
        private Double averagePace; // in seconds per 500m
        private Double averageSplit; // in seconds per 500m
        private Integer calories;
        private Integer averageStrokeRate;
    }
} 