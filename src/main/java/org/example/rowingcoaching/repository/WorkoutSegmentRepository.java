package org.example.rowingcoaching.repository;

import org.example.rowingcoaching.model.WorkoutSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutSegmentRepository extends JpaRepository<WorkoutSegment, Long> {
    List<WorkoutSegment> findByWorkoutIdOrderByOrderIndexAsc(Long workoutId);
} 