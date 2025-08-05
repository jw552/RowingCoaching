package org.example.rowingcoaching.repository;
import org.example.rowingcoaching.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByAthleteId(Long userId);
    List<Workout> findByAthleteIdOrderByCreatedAtDesc(Long userId);
    List<Workout> findByAthleteIdAndStatusInOrderByCreatedAtDesc(Long userId, List<Workout.WorkoutStatus> statuses);
    List<Workout> findByAthleteIdAndStatusOrderByCompletedAtDesc(Long userId, Workout.WorkoutStatus status);
    List<Workout> findByCoachIdOrderByCreatedAtDesc(Long coachId);
}
