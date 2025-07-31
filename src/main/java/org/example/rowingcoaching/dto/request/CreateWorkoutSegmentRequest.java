package org.example.rowingcoaching.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.rowingcoaching.model.WorkoutSegment;

@Getter
@Setter
public class CreateWorkoutSegmentRequest {

        @NotNull(message = "Segment type is required")
        private WorkoutSegment.SegmentType type;

        @NotNull(message = "Order index is required")
        @Min(value = 0, message = "Order index must be at least 0")
        private int orderIndex;

        // For workout segments - either distance OR duration must be provided
        @Min(value = 1, message = "Distance must be at least 1 meter")
        private Integer distance; // in meters

        @Min(value = 1, message = "Duration must be at least 1 second")
        private Integer duration; // in seconds

        @Min(value = 1, message = "Stroke rate must be at least 1")
        private Integer strokeRate;

        private Double pace; // in seconds per 500 meters
} 