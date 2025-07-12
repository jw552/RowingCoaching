package org.example.rowingcoaching.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeamRequest {

        @NotBlank(message = "Team name is required")
        private String name;

        @NotNull(message = "Coach ID is required")
        private Long coachId;
}

