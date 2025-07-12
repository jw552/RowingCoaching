package org.example.rowingcoaching.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Workout {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int distance; // in meters
    private double pace; // in seconds per 500 meters
    private int strokeRate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String rawJsonData;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User athlete;
}
