package org.example.rowingcoaching.controller;

import org.example.rowingcoaching.model.Workout;
import org.example.rowingcoaching.service.WorkoutService;
import org.example.rowingcoaching.dto.WorkoutDTO;
import org.example.rowingcoaching.dto.request.CreateWorkoutRequest;
import org.example.rowingcoaching.mapper.WorkoutMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutMapper workoutMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<List<WorkoutDTO>> getWorkoutsByUser(@PathVariable Long userId) {
        List<Workout> workouts = workoutService.getWorkoutsByUser(userId);
        List<WorkoutDTO> workoutDTOs = workouts.stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workoutDTOs);
    }

    @GetMapping("/workout/{workoutId}")
    public ResponseEntity<WorkoutDTO> getWorkoutById(@PathVariable Long workoutId) {
        Workout workout = workoutService.getWorkoutById(workoutId);
        return ResponseEntity.ok(workoutMapper.toDTO(workout));
    }

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(@Valid @RequestBody CreateWorkoutRequest request) {
        Workout saved = workoutService.createWorkout(request);
        return ResponseEntity.ok(workoutMapper.toDTO(saved));
    }
}
