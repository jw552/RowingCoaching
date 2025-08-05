package org.example.rowingcoaching.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.rowingcoaching.dto.WorkoutDTO;
import org.example.rowingcoaching.dto.WorkoutResultDTO;
import org.example.rowingcoaching.dto.WorkoutSegmentDTO;
import org.example.rowingcoaching.model.Workout;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.dto.request.CreateWorkoutRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WorkoutMapper {

    @Autowired
    private WorkoutSegmentMapper segmentMapper;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WorkoutDTO toDTO(Workout workout) {
        List<WorkoutSegmentDTO> segmentDTOs = workout.getSegments().stream()
                .map(segmentMapper::toDTO)
                .collect(Collectors.toList());

        Map<String, Object> metrics = null;
        if (workout.getMetrics() != null) {
            try {
                metrics = objectMapper.readValue(workout.getMetrics(), new TypeReference<Map<String, Object>>() {});
            } catch (JsonProcessingException e) {
                // Handle error or return empty map
                metrics = Map.of();
            }
        }

        return new WorkoutDTO(
                workout.getId(),
                workout.getType(),
                metrics,
                workout.getStatus(),
                workout.getCreatedAt(),
                workout.getStartedAt(),
                workout.getCompletedAt(),
                workout.getTotalDistance(),
                workout.getTotalTime(),
                workout.getAveragePace(),
                workout.getAverageSplit(),
                workout.getCalories(),
                workout.getAverageStrokeRate(),
                workout.getAthlete() != null ? workout.getAthlete().getId() : null,
                workout.getAthlete() != null ? workout.getAthlete().getUsername() : null,
                workout.getCoach() != null ? workout.getCoach().getId() : null,
                workout.getCoach() != null ? workout.getCoach().getUsername() : null,
                segmentDTOs
        );
    }

    public WorkoutResultDTO toResultDTO(Workout workout) {
        return new WorkoutResultDTO(
                workout.getId(),
                workout.getType() != null ? workout.getType().name() : "UNKNOWN",
                workout.getCompletedAt(),
                workout.getTotalDistance(),
                workout.getTotalTime(),
                workout.getAveragePace(),
                workout.getAverageSplit(),
                workout.getCalories(),
                workout.getAverageStrokeRate()
        );
    }

    public Workout fromCreateRequest(CreateWorkoutRequest request, User user) {
        Workout workout = new Workout();
        workout.setType(request.getType());
        workout.setAthlete(user);
        workout.setCreatedAt(java.time.LocalDateTime.now());
        workout.setStatus(Workout.WorkoutStatus.CREATED);
        
        // Convert metrics to JSON string
        if (request.getMetrics() != null) {
            try {
                workout.setMetrics(objectMapper.writeValueAsString(request.getMetrics()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize workout metrics", e);
            }
        }
        
        return workout;
    }
}
