package org.example.rowingcoaching.service;

import lombok.RequiredArgsConstructor;
import org.example.rowingcoaching.dto.WorkoutDTO;
import org.example.rowingcoaching.dto.WorkoutResultDTO;
import org.example.rowingcoaching.dto.request.*;
import org.example.rowingcoaching.mapper.WorkoutMapper;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.model.Workout;
import org.example.rowingcoaching.repository.UserRepository;
import org.example.rowingcoaching.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final WorkoutMapper workoutMapper;

    @Transactional
    public Workout createWorkout(CreateWorkoutRequest request, User currentUser) {
        Workout workout = workoutMapper.fromCreateRequest(request, currentUser);
        
        // If coach is assigning to an athlete
        if (request.getAthleteId() != null) {
            User athlete = userRepository.findById(request.getAthleteId())
                    .orElseThrow(() -> new RuntimeException("Athlete not found"));
            workout.setAthlete(athlete);
            workout.setCoach(currentUser);
            workout.setStatus(Workout.WorkoutStatus.ASSIGNED);
        }
        
        return workoutRepository.save(workout);
    }

    @Transactional
    public Workout startWorkout(Long workoutId, User currentUser) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found"));
        
        // Verify user has permission to start this workout
        if (!workout.getAthlete().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only start your own workouts");
        }
        
        if (workout.getStatus() != Workout.WorkoutStatus.CREATED && 
            workout.getStatus() != Workout.WorkoutStatus.ASSIGNED) {
            throw new RuntimeException("Workout cannot be started in its current state");
        }
        
        workout.setStatus(Workout.WorkoutStatus.IN_PROGRESS);
        workout.setStartedAt(LocalDateTime.now());
        
        return workoutRepository.save(workout);
    }

    @Transactional
    public Workout completeWorkout(Long workoutId, CompleteWorkoutRequest request, User currentUser) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found"));
        
        // Verify user has permission to complete this workout
        if (!workout.getAthlete().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only complete your own workouts");
        }
        
        if (workout.getStatus() != Workout.WorkoutStatus.IN_PROGRESS) {
            throw new RuntimeException("Workout must be in progress to be completed");
        }
        
        // Set workout results
        CompleteWorkoutRequest.WorkoutResult result = request.getResult();
        workout.setTotalDistance(result.getTotalDistance());
        workout.setTotalTime(result.getTotalTime());
        workout.setAveragePace(result.getAveragePace());
        workout.setAverageSplit(result.getAverageSplit());
        workout.setCalories(result.getCalories());
        workout.setAverageStrokeRate(result.getAverageStrokeRate());
        
        workout.setStatus(Workout.WorkoutStatus.COMPLETED);
        workout.setCompletedAt(LocalDateTime.now());
        
        return workoutRepository.save(workout);
    }

    @Transactional
    public Workout assignWorkout(Long workoutId, AssignWorkoutRequest request, User coach) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found"));
        
        // Verify coach has permission to assign this workout
        if (!workout.getCoach().getId().equals(coach.getId())) {
            throw new RuntimeException("You can only assign workouts you created");
        }
        
        User athlete = userRepository.findById(request.getAthleteId())
                .orElseThrow(() -> new RuntimeException("Athlete not found"));
        
        workout.setAthlete(athlete);
        workout.setStatus(Workout.WorkoutStatus.ASSIGNED);
        
        return workoutRepository.save(workout);
    }

    public List<WorkoutDTO> getMyWorkouts(User currentUser) {
        List<Workout> workouts = workoutRepository.findByAthleteIdOrderByCreatedAtDesc(currentUser.getId());
        return workouts.stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<WorkoutDTO> getAssignedWorkouts(User currentUser) {
        List<Workout> workouts = workoutRepository.findByAthleteIdAndStatusInOrderByCreatedAtDesc(
                currentUser.getId(), 
                List.of(Workout.WorkoutStatus.ASSIGNED, Workout.WorkoutStatus.IN_PROGRESS)
        );
        return workouts.stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<WorkoutResultDTO> getWorkoutResults(User currentUser) {
        List<Workout> completedWorkouts = workoutRepository.findByAthleteIdAndStatusOrderByCompletedAtDesc(
                currentUser.getId(), 
                Workout.WorkoutStatus.COMPLETED
        );
        return completedWorkouts.stream()
                .map(workoutMapper::toResultDTO)
                .collect(Collectors.toList());
    }

    public List<WorkoutDTO> getWorkoutsAssignedByCoach(User coach) {
        List<Workout> workouts = workoutRepository.findByCoachIdOrderByCreatedAtDesc(coach.getId());
        return workouts.stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WorkoutDTO getWorkoutById(Long workoutId, User currentUser) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found"));
        
        // Verify user has permission to view this workout
        if (!workout.getAthlete().getId().equals(currentUser.getId()) && 
            !workout.getCoach().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't have permission to view this workout");
        }
        
        return workoutMapper.toDTO(workout);
    }

    public Workout saveWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }
}