# Time Tracker

Minimal Spring Boot + Thymeleaf app for local time tracking with Postgres and Flyway.

## Features
- Streams: create/list/deactivate (case-insensitive uniqueness).
- Time entries: create/list/edit/delete.
- Analysis: date-range totals by stream and by task with readable `Hh Mm` formatting plus charts (Chart.js) for stream/task breakdowns.
- Flyway migrations included.
- Docker Compose for local Postgres (and optional app container).

## Prerequisites
- Java 21 (or 17).
- Maven 3.9+ (or use `./mvnw` if you add the wrapper).
- Docker if you want to use `docker compose`.

## Quickstart (local DB)
1) Start Postgres (via compose):
```bash
docker compose up -d db
```
2) Run the app:
```bash
mvn spring-boot:run
```
3) Open http://localhost:8080

### Environment overrides
```
DB_URL=jdbc:postgresql://localhost:5432/time_tracker
DB_USER=time_tracker
DB_PASSWORD=time_tracker
```

## Dockerized app
Build and run both services:
```bash
docker compose up --build
```

## Deployment hint (free-ish)
Use a free Postgres (Neon/Supabase) and deploy the container to Fly.io/Cloud Run. Set `DB_URL`, `DB_USER`, `DB_PASSWORD`, and ensure port 8080 is exposed.

## Endpoints (Thymeleaf)
- `/streams` list
- `/streams/new` create
- `/streams/{id}/deactivate` deactivate
- `/entries` list
- `/entries/new` create
- `/entries/{id}/edit` edit
- `/entries/{id}/delete` delete
- `/analysis` run reports

## Database schema
Flyway migration `V1__init.sql` creates:
- `streams` (`id` UUID PK, `name` unique lower-case index, `description`, `is_active`, timestamps)
- `time_entries` (`id` UUID PK, FK to streams, `task_name`, `entry_date`, optional `start_time`/`end_time`, `duration_minutes` check >=1, `notes`, timestamps)

## Tests
```bash
mvn test
```

## Notes
- Duration is the source of truth for analysis; start/end times are optional and validated only if both provided.
- Validation and inline errors are rendered on the same page.