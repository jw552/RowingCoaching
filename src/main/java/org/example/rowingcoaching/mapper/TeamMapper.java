package org.example.rowingcoaching.mapper;

import org.example.rowingcoaching.dto.TeamDTO;
import org.example.rowingcoaching.model.Team;
import org.example.rowingcoaching.dto.request.CreateTeamRequest;

public class TeamMapper {

    public static TeamDTO toDTO(Team team) {
        return new TeamDTO(
                team.getId(),
                team.getTeamName(),
                team.getTeamCode()
        );
    }

    public static Team fromCreateRequest(CreateTeamRequest request) {
        Team team = new Team();
        team.setTeamName(request.getName());
        team.setTeamCode(request.getTeamCode()); // Assuming this is in the request
        return team;
    }
}

