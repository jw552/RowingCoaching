package org.example.rowingcoaching.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignWorkoutRequest {
    @NotNull(message = "Athlete ID is required")
    private Long athleteId;
} 