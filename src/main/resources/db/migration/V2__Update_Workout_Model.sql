-- Update Workout table to match new model structure
-- This migration handles the transition from the old segment-based workout model to the new type-based model

-- Add new columns
ALTER TABLE workout ADD COLUMN type VARCHAR(20);
ALTER TABLE workout ADD COLUMN metrics TEXT;
ALTER TABLE workout ADD COLUMN status VARCHAR(20) DEFAULT 'CREATED';
ALTER TABLE workout ADD COLUMN created_at TIMESTAMP;
ALTER TABLE workout ADD COLUMN started_at TIMESTAMP;
ALTER TABLE workout ADD COLUMN completed_at TIMESTAMP;
ALTER TABLE workout ADD COLUMN total_time INTEGER;
ALTER TABLE workout ADD COLUMN average_split DOUBLE PRECISION;
ALTER TABLE workout ADD COLUMN calories INTEGER;
ALTER TABLE workout ADD COLUMN coach_id BIGINT;

-- Add foreign key for coach
ALTER TABLE workout ADD CONSTRAINT fk_workout_coach FOREIGN KEY (coach_id) REFERENCES app_user(id);

-- Rename existing columns to match new structure
ALTER TABLE workout RENAME COLUMN date TO created_at_old;
ALTER TABLE workout RENAME COLUMN total_duration TO total_time_old;
ALTER TABLE workout RENAME COLUMN user_id TO athlete_id;

-- Update existing records to have default values
UPDATE workout SET 
    type = 'SINGLE_DISTANCE',
    status = 'COMPLETED',
    created_at = created_at_old,
    total_time = total_time_old,
    average_split = average_pace,
    calories = 0;

-- Drop old columns
ALTER TABLE workout DROP COLUMN created_at_old;
ALTER TABLE workout DROP COLUMN total_time_old;
ALTER TABLE workout DROP COLUMN raw_json_data;

-- Add indexes for better performance
CREATE INDEX idx_workout_athlete_id ON workout(athlete_id);
CREATE INDEX idx_workout_coach_id ON workout(coach_id);
CREATE INDEX idx_workout_status ON workout(status);
CREATE INDEX idx_workout_created_at ON workout(created_at); 