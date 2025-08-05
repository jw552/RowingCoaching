package org.example.rowingcoaching;

import org.example.rowingcoaching.dto.request.CreateWorkoutRequest;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.model.Workout;
import org.example.rowingcoaching.service.WorkoutService;
import org.example.rowingcoaching.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class WorkoutSegmentTest {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateSingleDistanceWorkout() {
        // Create a test user first
        User testUser = createTestUser();
        
        // Create a single distance workout
        CreateWorkoutRequest request = new CreateWorkoutRequest();
        request.setType(Workout.WorkoutType.SINGLE_DISTANCE);
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("distance", 2000);
        request.setMetrics(metrics);

        var workout = workoutService.createWorkout(request, testUser);
        assertNotNull(workout);
        assertEquals(Workout.WorkoutType.SINGLE_DISTANCE, workout.getType());
        assertEquals(Workout.WorkoutStatus.CREATED, workout.getStatus());
        assertNotNull(workout.getCreatedAt());
    }

    @Test
    public void testCreateIntervalTimeWorkout() {
        // Create a test user first
        User testUser = createTestUser();
        
        // Create an interval time workout
        CreateWorkoutRequest request = new CreateWorkoutRequest();
        request.setType(Workout.WorkoutType.INTERVAL_TIME);
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("time", 300); // 5 minutes
        metrics.put("restPeriod", 60); // 1 minute rest
        request.setMetrics(metrics);

        var workout = workoutService.createWorkout(request, testUser);
        assertNotNull(workout);
        assertEquals(Workout.WorkoutType.INTERVAL_TIME, workout.getType());
        assertEquals(Workout.WorkoutStatus.CREATED, workout.getStatus());
    }

    @Test
    public void testStartAndCompleteWorkout() {
        // Create a test user first
        User testUser = createTestUser();
        
        // Create a workout
        CreateWorkoutRequest createRequest = new CreateWorkoutRequest();
        createRequest.setType(Workout.WorkoutType.SINGLE_DISTANCE);
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("distance", 1000);
        createRequest.setMetrics(metrics);

        var workout = workoutService.createWorkout(createRequest, testUser);
        
        // Start the workout
        var startedWorkout = workoutService.startWorkout(workout.getId(), testUser);
        assertEquals(Workout.WorkoutStatus.IN_PROGRESS, startedWorkout.getStatus());
        assertNotNull(startedWorkout.getStartedAt());
        
        // Complete the workout
        var completeRequest = new org.example.rowingcoaching.dto.request.CompleteWorkoutRequest();
        var result = new org.example.rowingcoaching.dto.request.CompleteWorkoutRequest.WorkoutResult();
        result.setTotalDistance(1000);
        result.setTotalTime(240); // 4 minutes
        result.setAveragePace(120.0); // 2:00/500m
        result.setAverageSplit(120.0);
        result.setCalories(120);
        result.setAverageStrokeRate(24);
        completeRequest.setResult(result);
        
        var completedWorkout = workoutService.completeWorkout(workout.getId(), completeRequest, testUser);
        assertEquals(Workout.WorkoutStatus.COMPLETED, completedWorkout.getStatus());
        assertNotNull(completedWorkout.getCompletedAt());
        assertEquals(1000, completedWorkout.getTotalDistance());
        assertEquals(240, completedWorkout.getTotalTime());
    }

    private User createTestUser() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        return userService.save(user);
    }
} 