package com.example.timetracker.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class TimeEntryForm {
    @NotNull(message = "Stream is required")
    private UUID streamId;

    @NotBlank(message = "Task name is required")
    @Size(max = 120, message = "Task name must be at most 120 characters")
    private String taskName;

    @NotNull(message = "Date is required")
    private LocalDate entryDate = LocalDate.now();

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    private LocalTime startTime;
    private LocalTime endTime;

    @Size(max = 5000, message = "Notes too long")
    private String notes;

    public UUID getStreamId() {
        return streamId;
    }

    public void setStreamId(UUID streamId) {
        this.streamId = streamId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

