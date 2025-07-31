package org.example.rowingcoaching.mapper;

import org.example.rowingcoaching.dto.WorkoutDTO;
import org.example.rowingcoaching.dto.WorkoutSegmentDTO;
import org.example.rowingcoaching.model.Workout;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.dto.request.CreateWorkoutRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkoutMapper {

    @Autowired
    private WorkoutSegmentMapper segmentMapper;

    public WorkoutDTO toDTO(Workout workout) {
        List<WorkoutSegmentDTO> segmentDTOs = workout.getSegments().stream()
                .map(segmentMapper::toDTO)
                .collect(Collectors.toList());

        return new WorkoutDTO(
                workout.getId(),
                workout.getAthlete() != null ? workout.getAthlete().getId() : null,
                workout.getDate(),
                workout.getTotalDistance(),
                workout.getTotalDuration(),
                workout.getAveragePace(),
                workout.getAverageStrokeRate(),
                workout.getRawJsonData(),
                segmentDTOs
        );
    }

    public Workout fromCreateRequest(CreateWorkoutRequest request, User user) {
        Workout workout = new Workout();
        workout.setAthlete(user);
        workout.setDate(request.getDate());
        workout.setRawJsonData(request.getRawJsonData());
        return workout;
    }
}
