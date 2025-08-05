package org.example.rowingcoaching.controller;

import lombok.RequiredArgsConstructor;
import org.example.rowingcoaching.dto.WorkoutDTO;
import org.example.rowingcoaching.dto.WorkoutResultDTO;
import org.example.rowingcoaching.dto.request.*;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.service.WorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(
            @Valid @RequestBody CreateWorkoutRequest request,
            @AuthenticationPrincipal User currentUser) {
        var workout = workoutService.createWorkout(request, currentUser);
        return ResponseEntity.ok(workoutService.getWorkoutById(workout.getId(), currentUser));
    }

    @GetMapping("/my")
    public ResponseEntity<List<WorkoutDTO>> getMyWorkouts(@AuthenticationPrincipal User currentUser) {
        List<WorkoutDTO> workouts = workoutService.getMyWorkouts(currentUser);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<WorkoutDTO>> getAssignedWorkouts(@AuthenticationPrincipal User currentUser) {
        List<WorkoutDTO> workouts = workoutService.getAssignedWorkouts(currentUser);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/results")
    public ResponseEntity<List<WorkoutResultDTO>> getWorkoutResults(@AuthenticationPrincipal User currentUser) {
        List<WorkoutResultDTO> results = workoutService.getWorkoutResults(currentUser);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutDTO> getWorkoutById(
            @PathVariable Long workoutId,
            @AuthenticationPrincipal User currentUser) {
        WorkoutDTO workout = workoutService.getWorkoutById(workoutId, currentUser);
        return ResponseEntity.ok(workout);
    }

    @PostMapping("/{workoutId}/start")
    public ResponseEntity<WorkoutDTO> startWorkout(
            @PathVariable Long workoutId,
            @Valid @RequestBody StartWorkoutRequest request,
            @AuthenticationPrincipal User currentUser) {
        var workout = workoutService.startWorkout(workoutId, currentUser);
        return ResponseEntity.ok(workoutService.getWorkoutById(workout.getId(), currentUser));
    }

    @PostMapping("/{workoutId}/complete")
    public ResponseEntity<WorkoutResultDTO> completeWorkout(
            @PathVariable Long workoutId,
            @Valid @RequestBody CompleteWorkoutRequest request,
            @AuthenticationPrincipal User currentUser) {
        var workout = workoutService.completeWorkout(workoutId, request, currentUser);
        return ResponseEntity.ok(workoutService.getWorkoutResults(currentUser)
                .stream()
                .filter(result -> result.id().equals(workoutId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Workout result not found")));
    }

    @PostMapping("/{workoutId}/assign")
    public ResponseEntity<WorkoutDTO> assignWorkout(
            @PathVariable Long workoutId,
            @Valid @RequestBody AssignWorkoutRequest request,
            @AuthenticationPrincipal User currentUser) {
        var workout = workoutService.assignWorkout(workoutId, request, currentUser);
        return ResponseEntity.ok(workoutService.getWorkoutById(workout.getId(), currentUser));
    }

    // Legacy endpoints for backward compatibility
    @GetMapping("/{userId}")
    public ResponseEntity<List<WorkoutDTO>> getWorkoutsByUser(@PathVariable Long userId) {
        // This endpoint is deprecated - use /my instead
        throw new UnsupportedOperationException("Use /my endpoint instead");
    }

    @GetMapping("/workout/{workoutId}")
    public ResponseEntity<WorkoutDTO> getWorkoutByIdLegacy(@PathVariable Long workoutId) {
        // This endpoint is deprecated - use /{workoutId} instead
        throw new UnsupportedOperationException("Use /{workoutId} endpoint instead");
    }
}
