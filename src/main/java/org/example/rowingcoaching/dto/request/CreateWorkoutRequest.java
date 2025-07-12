package org.example.rowingcoaching.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateWorkoutRequest {

        @NotNull(message = "User ID is required")
        private Long userId;

        @NotNull(message = "Date is required")
        private LocalDate date;

        @Min(value = 1, message = "Duration must be at least 1 second")
        private int duration;

        @Min(value = 1, message = "Distance must be at least 1 meter")
        private int distance;

        @Min(value = 1, message = "Stroke rate must be at least 1")
        private int strokeRate;

        private double pace;

        private String rawJsonData;
}
