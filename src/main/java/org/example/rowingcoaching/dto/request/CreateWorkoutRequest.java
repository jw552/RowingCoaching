package org.example.rowingcoaching.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.rowingcoaching.model.Workout;

import java.util.Map;

@Getter
@Setter
public class CreateWorkoutRequest {

    @NotNull(message = "Workout type is required")
    private Workout.WorkoutType type;

    @NotNull(message = "Workout metrics are required")
    private Map<String, Object> metrics;

    // Optional: for coach assigning to athlete
    private Long athleteId;
}
