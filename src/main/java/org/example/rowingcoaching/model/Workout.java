package org.example.rowingcoaching.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Workout {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int totalDistance; // in meters - calculated from segments
    private int totalDuration; // in seconds - calculated from segments
    private double averagePace; // in seconds per 500 meters - calculated from segments
    private int averageStrokeRate; // calculated from segments

    @Lob
    @Column(columnDefinition = "TEXT")
    private String rawJsonData;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User athlete;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    private List<WorkoutSegment> segments = new ArrayList<>();
}
