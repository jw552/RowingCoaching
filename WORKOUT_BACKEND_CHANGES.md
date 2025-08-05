# Backend Changes for Workout Functionality

This document summarizes the backend changes made to support the frontend workout functionality with ErgometerJS integration.

## Overview

The backend has been updated to support a new workout model that aligns with the frontend's workout type system and ErgometerJS integration. The changes include:

1. **New Workout Model Structure**
2. **Workout Assignment System**
3. **Workout Results/Logbook**
4. **Updated API Endpoints**
5. **Database Migration**

## Key Changes

### 1. Updated Workout Model (`src/main/java/org/example/rowingcoaching/model/Workout.java`)

**New Fields:**
- `type`: WorkoutType enum (SINGLE_DISTANCE, SINGLE_TIME, INTERVAL_DISTANCE, INTERVAL_TIME)
- `metrics`: JSON string for workout parameters (distance, time, restPeriod, etc.)
- `status`: WorkoutStatus enum (CREATED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED)
- `createdAt`, `startedAt`, `completedAt`: Timestamps for workout lifecycle
- `totalTime`, `averageSplit`, `calories`: Additional result fields
- `coach`: Reference to coach who assigned the workout

**Removed Fields:**
- `date` (replaced with `createdAt`)
- `totalDuration` (replaced with `totalTime`)
- `rawJsonData` (replaced with `metrics`)

### 2. New DTOs and Request Objects

**Created:**
- `WorkoutResultDTO`: For workout results in logbook
- `StartWorkoutRequest`: For starting workouts
- `CompleteWorkoutRequest`: For completing workouts with results
- `AssignWorkoutRequest`: For assigning workouts to athletes

**Updated:**
- `CreateWorkoutRequest`: Now uses workout type and metrics instead of segments
- `WorkoutDTO`: Updated to match new model structure

### 3. Updated WorkoutService (`src/main/java/org/example/rowingcoaching/service/WorkoutService.java`)

**New Methods:**
- `createWorkout(CreateWorkoutRequest, User)`: Creates workouts with type and metrics
- `startWorkout(Long, User)`: Starts a workout
- `completeWorkout(Long, CompleteWorkoutRequest, User)`: Completes workout with results
- `assignWorkout(Long, AssignWorkoutRequest, User)`: Assigns workout to athlete
- `getMyWorkouts(User)`: Gets user's own workouts
- `getAssignedWorkouts(User)`: Gets workouts assigned to user
- `getWorkoutResults(User)`: Gets completed workout results
- `getWorkoutsAssignedByCoach(User)`: Gets workouts assigned by coach

**Removed Methods:**
- Segment-based workout creation methods
- Old validation and calculation methods

### 4. Updated WorkoutController (`src/main/java/org/example/rowingcoaching/controller/WorkoutController.java`)

**New Endpoints:**
- `POST /api/workouts` - Create workout
- `GET /api/workouts/my` - Get user's workouts
- `GET /api/workouts/assigned` - Get assigned workouts
- `GET /api/workouts/results` - Get workout results/logbook
- `GET /api/workouts/{workoutId}` - Get specific workout
- `POST /api/workouts/{workoutId}/start` - Start workout
- `POST /api/workouts/{workoutId}/complete` - Complete workout
- `POST /api/workouts/{workoutId}/assign` - Assign workout to athlete

### 5. Updated WorkoutRepository (`src/main/java/org/example/rowingcoaching/repository/WorkoutRepository.java`)

**New Query Methods:**
- `findByAthleteIdOrderByCreatedAtDesc(Long)`
- `findByAthleteIdAndStatusInOrderByCreatedAtDesc(Long, List<WorkoutStatus>)`
- `findByAthleteIdAndStatusOrderByCompletedAtDesc(Long, WorkoutStatus)`
- `findByCoachIdOrderByCreatedAtDesc(Long)`

### 6. Updated WorkoutMapper (`src/main/java/org/example/rowingcoaching/mapper/WorkoutMapper.java`)

**New Features:**
- JSON serialization/deserialization for metrics
- Support for workout results DTO
- Updated mapping for new model structure

### 7. Enhanced UserService (`src/main/java/org/example/rowingcoaching/service/UserService.java`)

**New Method:**
- `isUserCoachOfAnyTeam(Long)`: Checks if user is a coach of any team

### 8. Enhanced UserTeamRepository (`src/main/java/org/example/rowingcoaching/repository/UserTeamRepository.java`)

**New Method:**
- `existsActiveByUserIdAndRole(Long, UserTeam.Role)`: Checks if user has specific role

### 9. Database Migration (`src/main/resources/db/migration/V2__Update_Workout_Model.sql`)

**Changes:**
- Adds new columns for workout type, metrics, status, timestamps
- Renames existing columns to match new structure
- Adds foreign key for coach relationship
- Adds performance indexes
- Migrates existing data to new structure

### 10. Updated Tests (`src/test/java/org/example/rowingcoaching/WorkoutSegmentTest.java`)

**New Tests:**
- `testCreateSingleDistanceWorkout()`: Tests single distance workout creation
- `testCreateIntervalTimeWorkout()`: Tests interval time workout creation
- `testStartAndCompleteWorkout()`: Tests workout lifecycle

## API Endpoints Summary

### Workout Management
- `POST /api/workouts` - Create new workout
- `GET /api/workouts/my` - Get user's workouts
- `GET /api/workouts/assigned` - Get assigned workouts
- `GET /api/workouts/{workoutId}` - Get specific workout

### Workout Execution
- `POST /api/workouts/{workoutId}/start` - Start workout
- `POST /api/workouts/{workoutId}/complete` - Complete workout with results

### Workout Assignment (Coach Only)
- `POST /api/workouts/{workoutId}/assign` - Assign workout to athlete

### Workout Results
- `GET /api/workouts/results` - Get workout logbook/results

## Frontend Integration

The backend now fully supports the frontend's workout functionality:

1. **Workout Types**: Supports all workout types (SINGLE_DISTANCE, SINGLE_TIME, INTERVAL_DISTANCE, INTERVAL_TIME)
2. **ErgometerJS Integration**: Ready for real-time data from Concept2 ergometers
3. **Coach/Athlete Assignment**: Supports workout assignment system
4. **Workout Results**: Tracks and stores workout completion data
5. **User Roles**: Supports coach and athlete role checking

## Migration Notes

- Existing workout data will be migrated to the new structure
- Old segment-based workouts will be converted to SINGLE_DISTANCE type
- No data loss during migration
- Backward compatibility maintained where possible

## Next Steps

1. Run the database migration
2. Test the new API endpoints
3. Verify frontend integration
4. Test ErgometerJS connectivity
5. Validate coach/athlete assignment functionality 