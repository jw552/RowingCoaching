package org.example.rowingcoaching.mapper;

import org.example.rowingcoaching.dto.WorkoutSegmentDTO;
import org.example.rowingcoaching.model.WorkoutSegment;
import org.springframework.stereotype.Component;

@Component
public class WorkoutSegmentMapper {

    public WorkoutSegmentDTO toDTO(WorkoutSegment segment) {
        return new WorkoutSegmentDTO(
                segment.getId(),
                segment.getType(),
                segment.getOrderIndex(),
                segment.getDistance(),
                segment.getDuration(),
                segment.getPace(),
                segment.getStrokeRate()
        );
    }

    public WorkoutSegment toEntity(WorkoutSegmentDTO dto) {
        WorkoutSegment segment = new WorkoutSegment();
        segment.setId(dto.id());
        segment.setType(dto.type());
        segment.setOrderIndex(dto.orderIndex());
        segment.setDistance(dto.distance());
        segment.setDuration(dto.duration());
        segment.setPace(dto.pace());
        segment.setStrokeRate(dto.strokeRate());
        return segment;
    }
} 