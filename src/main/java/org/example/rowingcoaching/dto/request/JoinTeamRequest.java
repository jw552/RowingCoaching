package org.example.rowingcoaching.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinTeamRequest {

    @NotBlank(message = "Team code is required")
    private String teamCode;
}