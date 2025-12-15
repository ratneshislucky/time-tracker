CREATE TABLE streams (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE UNIQUE INDEX uq_streams_name_lower ON streams (LOWER(name));

CREATE TABLE time_entries (
    id UUID PRIMARY KEY,
    stream_id UUID NOT NULL REFERENCES streams(id),
    task_name VARCHAR(120) NOT NULL,
    entry_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    duration_minutes INT NOT NULL CHECK (duration_minutes >= 1),
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_time_entries_date ON time_entries(entry_date);

