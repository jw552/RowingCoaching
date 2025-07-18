package org.example.rowingcoaching.mapper;

import org.example.rowingcoaching.dto.UserDTO;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.dto.request.CreateUserRequest;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public static User fromCreateRequest(CreateUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }
}

