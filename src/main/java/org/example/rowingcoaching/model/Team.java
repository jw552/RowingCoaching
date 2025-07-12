package org.example.rowingcoaching.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String teamCode;

    private String teamName;

    @OneToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "id", unique = true)
    private User coach;

    @OneToMany(mappedBy = "team")
    private List<User> members = new ArrayList<>();

    // Getters and setters
}
