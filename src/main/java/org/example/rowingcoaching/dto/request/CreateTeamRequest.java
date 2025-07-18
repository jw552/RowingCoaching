package org.example.rowingcoaching.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeamRequest {

        @NotBlank(message = "Team name is required")
        private String name;

        @NotBlank(message = "Team code is required")
        private String teamCode;
}