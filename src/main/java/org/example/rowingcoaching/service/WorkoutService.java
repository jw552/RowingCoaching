package org.example.rowingcoaching.service;

import lombok.RequiredArgsConstructor;
import org.example.rowingcoaching.dto.request.CreateWorkoutRequest;
import org.example.rowingcoaching.dto.request.CreateWorkoutSegmentRequest;
import org.example.rowingcoaching.mapper.WorkoutMapper;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.model.Workout;
import org.example.rowingcoaching.model.WorkoutSegment;
import org.example.rowingcoaching.repository.UserRepository;
import org.example.rowingcoaching.repository.WorkoutRepository;
import org.example.rowingcoaching.repository.WorkoutSegmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutSegmentRepository segmentRepository;
    private final UserRepository userRepository;
    private final WorkoutMapper workoutMapper;

    @Transactional
    public Workout createWorkout(CreateWorkoutRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate segments
        validateSegments(request.getSegments());

        // Create main workout
        Workout workout = workoutMapper.fromCreateRequest(request, user);
        workout = workoutRepository.save(workout);

        // Create and save segments
        List<WorkoutSegment> segments = createAndSaveSegments(request.getSegments(), workout);
        
        // Set segments and calculate totals
        workout.setSegments(segments);
        calculateAndUpdateTotals(workout);
        
        return workoutRepository.save(workout);
    }

    private List<WorkoutSegment> createAndSaveSegments(List<CreateWorkoutSegmentRequest> segmentRequests, Workout workout) {
        List<WorkoutSegment> segments = segmentRequests.stream()
                .map(segmentRequest -> createSegment(segmentRequest, workout))
                .toList();
        
        return segmentRepository.saveAll(segments);
    }

    private WorkoutSegment createSegment(CreateWorkoutSegmentRequest request, Workout workout) {
        WorkoutSegment segment = new WorkoutSegment();
        segment.setWorkout(workout);
        segment.setType(request.getType());
        segment.setOrderIndex(request.getOrderIndex());
        segment.setDuration(request.getDuration());

        if (request.getType() == WorkoutSegment.SegmentType.WORKOUT) {
            // For workout segments, validate that either distance OR duration is provided
            if (request.getDistance() == null && request.getDuration() == null) {
                throw new IllegalArgumentException("For workout segments, either distance or duration must be provided");
            }
            if (request.getDistance() != null && request.getDuration() != null) {
                throw new IllegalArgumentException("For workout segments, only one of distance or duration should be provided");
            }

            segment.setDistance(request.getDistance());
            segment.setStrokeRate(request.getStrokeRate());
            segment.setPace(request.getPace());

            // Calculate missing field based on pace
            if (request.getDistance() != null && request.getDuration() == null && request.getPace() != null) {
                // Calculate duration from distance and pace
                double durationSeconds = (request.getDistance() / 500.0) * request.getPace();
                segment.setDuration((int) Math.round(durationSeconds));
            } else if (request.getDuration() != null && request.getDistance() == null && request.getPace() != null) {
                // Calculate distance from duration and pace
                double distanceMeters = (request.getDuration() / request.getPace()) * 500.0;
                segment.setDistance((int) Math.round(distanceMeters));
            }
        } else {
            // For rest segments, only duration is valid
            if (request.getDistance() != null || request.getStrokeRate() != null || request.getPace() != null) {
                throw new IllegalArgumentException("Rest segments should only have duration");
            }
            segment.setDistance(null);
            segment.setStrokeRate(null);
            segment.setPace(null);
        }

        return segment;
    }

    private void validateSegments(List<CreateWorkoutSegmentRequest> segments) {
        if (segments == null || segments.isEmpty()) {
            throw new IllegalArgumentException("At least one segment is required");
        }

        // Check for duplicate order indices
        long uniqueOrderIndices = segments.stream()
                .mapToInt(CreateWorkoutSegmentRequest::getOrderIndex)
                .distinct()
                .count();
        if (uniqueOrderIndices != segments.size()) {
            throw new IllegalArgumentException("Order indices must be unique");
        }
    }

    private void calculateAndUpdateTotals(Workout workout) {
        List<WorkoutSegment> workoutSegments = workout.getSegments().stream()
                .filter(segment -> segment.getType() == WorkoutSegment.SegmentType.WORKOUT)
                .toList();

        // Calculate totals
        int totalDistance = workoutSegments.stream()
                .mapToInt(segment -> segment.getDistance() != null ? segment.getDistance() : 0)
                .sum();

        int totalDuration = workout.getSegments().stream()
                .mapToInt(WorkoutSegment::getDuration)
                .sum();

        double averagePace = 0.0;
        int averageStrokeRate = 0;
        int workoutSegmentCount = workoutSegments.size();

        if (workoutSegmentCount > 0) {
            double totalPace = workoutSegments.stream()
                    .mapToDouble(segment -> segment.getPace() != null ? segment.getPace() : 0.0)
                    .sum();
            averagePace = totalPace / workoutSegmentCount;

            int totalStrokeRate = workoutSegments.stream()
                    .mapToInt(segment -> segment.getStrokeRate() != null ? segment.getStrokeRate() : 0)
                    .sum();
            averageStrokeRate = totalStrokeRate / workoutSegmentCount;
        }

        workout.setTotalDistance(totalDistance);
        workout.setTotalDuration(totalDuration);
        workout.setAveragePace(averagePace);
        workout.setAverageStrokeRate(averageStrokeRate);
    }

    public Workout saveWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    public List<Workout> getWorkoutsByUser(Long userId) {
        return workoutRepository.findByAthleteId(userId);
    }

    public Workout getWorkoutById(Long workoutId) {
        return workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found"));
    }
}