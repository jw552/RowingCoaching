package org.example.rowingcoaching.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.rowingcoaching.model.User;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private User user;
}
