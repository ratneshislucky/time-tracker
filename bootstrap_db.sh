#!/usr/bin/env bash
set -euo pipefail

# Connection as a superuser (adjust PGUSER/PGPASSWORD if your superuser differs)
PGHOST=${PGHOST:-localhost}
PGPORT=${PGPORT:-5432}
PGUSER=${PGUSER:-postgres}
PGPASSWORD=${PGPASSWORD:-postgres}

export PGHOST PGPORT PGUSER PGPASSWORD

DB_NAME="time_tracker"
DB_USER="time_tracker"
DB_PASS="time_tracker"

echo "Creating role and database if missing..."
psql -v ON_ERROR_STOP=1 <<'SQL'
DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'time_tracker') THEN
    CREATE ROLE time_tracker LOGIN PASSWORD 'time_tracker';
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'time_tracker') THEN
    CREATE DATABASE time_tracker OWNER time_tracker;
  END IF;
END$$;
SQL

echo "Applying schema to ${DB_NAME}..."
psql -v ON_ERROR_STOP=1 -d "${DB_NAME}" <<'SQL'
CREATE TABLE IF NOT EXISTS streams (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_streams_name_lower ON streams (LOWER(name));

CREATE TABLE IF NOT EXISTS time_entries (
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

CREATE INDEX IF NOT EXISTS idx_time_entries_date ON time_entries(entry_date);
SQL

echo "Done. DB user=${DB_USER}, db=${DB_NAME}, host=${PGHOST}, port=${PGPORT}"