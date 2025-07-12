package org.example.rowingcoaching.mapper;

import org.example.rowingcoaching.dto.TeamDTO;
import org.example.rowingcoaching.model.Team;
import org.example.rowingcoaching.dto.request.CreateTeamRequest;
import org.example.rowingcoaching.model.User;


public class TeamMapper {

    public static TeamDTO toDTO(Team team) {
        return new TeamDTO(
                team.getId(),
                team.getTeamName(),
                team.getTeamCode(),
                team.getCoach() != null ? team.getCoach().getId() : null
        );
    }

    public static Team fromCreateRequest(CreateTeamRequest request, User coach) {
        Team team = new Team();
        team.setTeamName(request.getName());
        team.setCoach(coach); // Must be looked up by ID before calling
        return team;
    }
}

