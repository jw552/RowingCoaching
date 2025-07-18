package org.example.rowingcoaching.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rowingcoaching.dto.UserDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private UserDTO user;
    private String errorMessage;

    public AuthResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
        this.errorMessage = null;
    }
}
