package org.example.rowingcoaching.dto;

import org.example.rowingcoaching.model.WorkoutSegment;

public record WorkoutSegmentDTO(
        Long id,
        WorkoutSegment.SegmentType type,
        int orderIndex,
        Integer distance,
        Integer duration,
        Double pace,
        Integer strokeRate
) {} 