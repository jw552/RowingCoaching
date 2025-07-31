package org.example.rowingcoaching.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateWorkoutRequest {

        @NotNull(message = "User ID is required")
        private Long userId;

        @NotNull(message = "Date is required")
        private LocalDate date;

        @NotNull(message = "At least one segment is required")
        private List<CreateWorkoutSegmentRequest> segments;

        private String rawJsonData;
}
