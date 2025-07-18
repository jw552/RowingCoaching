package org.example.rowingcoaching.service;

import lombok.RequiredArgsConstructor;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.model.UserTeam;
import org.example.rowingcoaching.repository.UserRepository;
import org.example.rowingcoaching.repository.UserTeamRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        System.out.println("[USER DETAILS SERVICE] Looking for: " + usernameOrEmail);

        User user = null;

        // First try to find by username or email
        try {
            user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                    .orElse(null);
            if (user != null) {
                System.out.println("[USER DETAILS SERVICE] Found user by username/email: " + user.getUsername());
            }
        } catch (Exception e) {
            System.out.println("[USER DETAILS SERVICE] Error finding by username/email: " + e.getMessage());
        }

        // If not found, try to parse as ID (in case Spring Security is using user ID)
        if (user == null) {
            try {
                Long userId = Long.parseLong(usernameOrEmail);
                user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    System.out.println("[USER DETAILS SERVICE] Found user by ID: " + userId);
                }
            } catch (NumberFormatException e) {
                // Not a number, ignore
                System.out.println("[USER DETAILS SERVICE] Not a valid user ID: " + usernameOrEmail);
            }
        }

        if (user == null) {
            System.out.println("[USER DETAILS SERVICE] User not found: " + usernameOrEmail);
            throw new UsernameNotFoundException("User not found: " + usernameOrEmail);
        }

        // Get all active team relationships for this user
        List<UserTeam> userTeams = userTeamRepository.findActiveByUserId(user.getId());

        // Build authorities from all team roles
        Collection<GrantedAuthority> authorities = buildAuthorities(userTeams);

        System.out.println("[USER DETAILS SERVICE] Successfully loaded user: " + user.getUsername() +
                " with authorities: " + authorities +
                " and " + userTeams.size() + " active team(s)");

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    /**
     * Build Spring Security authorities from user's team roles
     */
    private Collection<GrantedAuthority> buildAuthorities(List<UserTeam> userTeams) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (userTeams.isEmpty()) {
            // If user has no teams, give them a basic USER role
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            System.out.println("[USER DETAILS SERVICE] User has no teams, assigned ROLE_USER");
        } else {
            // Add role-based authorities
            for (UserTeam userTeam : userTeams) {
                String roleAuthority = "ROLE_" + userTeam.getRole().name().toUpperCase();
                authorities.add(new SimpleGrantedAuthority(roleAuthority));

                // You can also add team-specific authorities if needed
                String teamRoleAuthority = "ROLE_" + userTeam.getRole().name().toUpperCase() +
                        "_TEAM_" + userTeam.getTeam().getId();
                authorities.add(new SimpleGrantedAuthority(teamRoleAuthority));
            }

            // Add a general authenticated user role
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            System.out.println("[USER DETAILS SERVICE] Built authorities from " + userTeams.size() + " team relationships");
        }

        return authorities;
    }
}