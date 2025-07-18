package org.example.rowingcoaching.mapper;

import org.example.rowingcoaching.dto.UserTeamDTO;
import org.example.rowingcoaching.dto.UserTeamDetailsDTO;
import org.example.rowingcoaching.model.UserTeam;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.model.Team;

public class UserTeamMapper {

    public static UserTeamDTO toDTO(UserTeam userTeam) {
        return new UserTeamDTO(
                userTeam.getId(),
                userTeam.getUser().getId(),
                userTeam.getTeam().getId(),
                userTeam.getRole().name(),
                userTeam.getJoinedDate(),
                userTeam.getLeftDate(),
                userTeam.getActive()
        );
    }

    public static UserTeamDetailsDTO toDetailsDTO(UserTeam userTeam) {
        return new UserTeamDetailsDTO(
                userTeam.getId(),
                UserMapper.toDTO(userTeam.getUser()),
                TeamMapper.toDTO(userTeam.getTeam()),
                userTeam.getRole().name(),
                userTeam.getJoinedDate(),
                userTeam.getLeftDate(),
                userTeam.getActive()
        );
    }

    public static UserTeam fromEntities(User user, Team team, UserTeam.Role role) {
        return new UserTeam(user, team, role);
    }
}