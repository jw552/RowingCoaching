package org.example.rowingcoaching.repository;
import org.example.rowingcoaching.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByAthleteId(Long userId);
}
