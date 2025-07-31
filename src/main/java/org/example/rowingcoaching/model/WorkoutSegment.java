package org.example.rowingcoaching.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WorkoutSegment {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SegmentType type; // WORKOUT or REST

    private int orderIndex; // Order within the workout

    // For workout segments
    private Integer distance; // in meters, null for rest
    private Integer duration; // in seconds
    private Double pace; // in seconds per 500 meters, null for rest
    private Integer strokeRate; // null for rest

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    public enum SegmentType {
        WORKOUT,
        REST
    }
} 