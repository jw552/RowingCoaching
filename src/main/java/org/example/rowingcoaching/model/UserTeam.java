package org.example.rowingcoaching.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_team")
public class UserTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime joinedDate;

    // Optional: if you want to track when someone left a team
    private LocalDateTime leftDate;

    // Optional: if you want to track active/inactive status
    @Column(nullable = false)
    private Boolean active = true;

    public enum Role {
        ATHLETE, COACH
    }

    // Constructor for easy creation
    public UserTeam(User user, Team team, Role role) {
        this.user = user;
        this.team = team;
        this.role = role;
        this.joinedDate = LocalDateTime.now();
        this.active = true;
    }
}