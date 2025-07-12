package org.example.rowingcoaching.mapper;

import org.example.rowingcoaching.dto.WorkoutDTO;
import org.example.rowingcoaching.model.Workout;
import org.example.rowingcoaching.model.User;
import org.example.rowingcoaching.dto.request.CreateWorkoutRequest;

public class WorkoutMapper {

    public static WorkoutDTO toDTO(Workout workout) {
        return new WorkoutDTO(
                workout.getId(),
                workout.getAthlete() != null ? workout.getAthlete().getId() : null,
                workout.getDate(),
                workout.getDistance(),
                workout.getStrokeRate(),
                workout.getPace(),
                workout.getRawJsonData()
        );
    }

    public static Workout fromCreateRequest(CreateWorkoutRequest request, User user) {
        Workout workout = new Workout();
        workout.setAthlete(user);
        workout.setDate(request.getDate());
        workout.setDistance(request.getDistance());
        workout.setStrokeRate(request.getStrokeRate());
        workout.setPace(request.getPace());
        workout.setRawJsonData(request.getRawJsonData());
        return workout;
    }
}
