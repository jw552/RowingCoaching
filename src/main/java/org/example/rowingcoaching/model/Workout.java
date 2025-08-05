package org.example.rowingcoaching.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Workout {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private WorkoutType type;

    @Column(columnDefinition = "TEXT")
    private String metrics; // JSON string for workout metrics (distance, time, restPeriod, etc.)

    @Enumerated(EnumType.STRING)
    private WorkoutStatus status = WorkoutStatus.CREATED;

    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    // Results from completed workout
    private Integer totalDistance; // in meters
    private Integer totalTime; // in seconds
    private Double averagePace; // in seconds per 500m
    private Double averageSplit; // in seconds per 500m
    private Integer calories;
    private Integer averageStrokeRate;

    @ManyToOne
    @JoinColumn(name = "athlete_id")
    private User athlete;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    private List<WorkoutSegment> segments = new ArrayList<>();

    public enum WorkoutType {
        SINGLE_DISTANCE,
        SINGLE_TIME,
        INTERVAL_DISTANCE,
        INTERVAL_TIME
    }

    public enum WorkoutStatus {
        CREATED,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}
