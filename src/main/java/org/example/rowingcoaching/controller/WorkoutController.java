package org.example.rowingcoaching.controller;

import org.example.rowingcoaching.model.Workout;
import org.example.rowingcoaching.service.WorkoutService;
import org.example.rowingcoaching.dto.WorkoutDTO;
import org.example.rowingcoaching.dto.request.CreateWorkoutRequest;
import org.example.rowingcoaching.mapper.WorkoutMapper;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;

    @GetMapping("/{userId}")
    public List<Workout> getWorkoutsByUser(@PathVariable Long userId) {
        return workoutService.getWorkoutsByUser(userId);
    }

    @PostMapping
    public ResponseEntity<WorkoutDTO> saveWorkout(@Valid @RequestBody CreateWorkoutRequest request) {
        User athlete = userService.getUserById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Workout workout = WorkoutMapper.fromCreateRequest(request, athlete);
        Workout saved = workoutService.saveWorkout(workout);
        return ResponseEntity.ok(WorkoutMapper.toDTO(saved));
    }
}
