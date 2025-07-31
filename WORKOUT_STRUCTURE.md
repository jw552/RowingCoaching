# Workout Structure Documentation

## Overview

The workout system has been redesigned to support complex cardio workouts with multiple segments, including both workout intervals and rest periods. Each workout consists of a main workout entity that contains aggregated data and a list of segments that represent the individual components of the workout.

## Database Structure

### Main Workout Table
- `id`: Primary key
- `date`: Date of the workout
- `total_distance`: Total distance in meters (calculated from segments)
- `total_duration`: Total duration in seconds (calculated from segments)
- `average_pace`: Average pace in seconds per 500m (calculated from segments)
- `average_stroke_rate`: Average stroke rate (calculated from segments)
- `raw_json_data`: Additional JSON data
- `user_id`: Foreign key to user

### Workout Segment Table
- `id`: Primary key
- `type`: Either "WORKOUT" or "REST"
- `order_index`: Order within the workout (0, 1, 2, etc.)
- `distance`: Distance in meters (null for rest segments)
- `duration`: Duration in seconds
- `pace`: Pace in seconds per 500m (null for rest segments)
- `stroke_rate`: Stroke rate (null for rest segments)
- `workout_id`: Foreign key to main workout

## API Usage

### Creating a Workout

**POST** `/api/workouts`

```json
{
  "userId": 1,
  "date": "2024-01-15",
  "segments": [
    {
      "type": "WORKOUT",
      "orderIndex": 0,
      "distance": 1000,
      "pace": 120.0,
      "strokeRate": 24
    },
    {
      "type": "REST",
      "orderIndex": 1,
      "duration": 60
    },
    {
      "type": "WORKOUT",
      "orderIndex": 2,
      "distance": 500,
      "pace": 125.0,
      "strokeRate": 26
    }
  ]
}
```

### Getting Workouts by User

**GET** `/api/workouts/{userId}`

Returns a list of workouts with their segments.

### Getting a Specific Workout

**GET** `/api/workouts/workout/{workoutId}`

Returns a specific workout with all its segments.

## Key Features

### 1. Duration vs Distance Logic
- For workout segments, users can specify either `distance` OR `duration`, not both
- The missing field is calculated based on the provided `pace`
- For rest segments, only `duration` is valid

### 2. Automatic Calculations
- `total_distance`: Sum of all workout segment distances
- `total_duration`: Sum of all segment durations (workout + rest)
- `average_pace`: Average of all workout segment paces
- `average_stroke_rate`: Average of all workout segment stroke rates

### 3. Validation
- At least one segment is required
- Order indices must be unique
- Rest segments cannot have distance, pace, or stroke rate
- Workout segments must have either distance or duration (not both)

### 4. Frontend Display Structure
The API response structure supports the following frontend layout:

```
Workout Summary (totalDistance, totalDuration, averagePace, averageStrokeRate)
├── Segment 1 (orderIndex: 0) - 1000m @ 2:00/500m, 24 spm
├── Segment 2 (orderIndex: 1) - Rest: 60 seconds
└── Segment 3 (orderIndex: 2) - 500m @ 2:05/500m, 26 spm
```

## Example Workout Types

### 1. Simple Distance Workout
```json
{
  "segments": [
    {
      "type": "WORKOUT",
      "orderIndex": 0,
      "distance": 2000,
      "pace": 130.0,
      "strokeRate": 22
    }
  ]
}
```

### 2. Interval Training
```json
{
  "segments": [
    {
      "type": "WORKOUT",
      "orderIndex": 0,
      "distance": 500,
      "pace": 115.0,
      "strokeRate": 28
    },
    {
      "type": "REST",
      "orderIndex": 1,
      "duration": 120
    },
    {
      "type": "WORKOUT",
      "orderIndex": 2,
      "distance": 500,
      "pace": 115.0,
      "strokeRate": 28
    },
    {
      "type": "REST",
      "orderIndex": 3,
      "duration": 120
    }
  ]
}
```

### 3. Time-Based Workout
```json
{
  "segments": [
    {
      "type": "WORKOUT",
      "orderIndex": 0,
      "duration": 1800,
      "pace": 125.0,
      "strokeRate": 24
    }
  ]
}
```

## Migration Notes

The existing workout table will be automatically updated by Hibernate. The old `distance`, `pace`, and `stroke_rate` columns will be renamed to `total_distance`, `average_pace`, and `average_stroke_rate`, and a new `total_duration` column will be added.

Existing workouts will need to be migrated to the new segment structure if you want to preserve their data in the new format. 