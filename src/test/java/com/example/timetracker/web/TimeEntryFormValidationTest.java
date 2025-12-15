package com.example.timetracker.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.timetracker.web.dto.TimeEntryForm;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import java.util.UUID;

class TimeEntryFormValidationTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void durationMustBePositive() {
        TimeEntryForm form = baseForm();
        form.setDurationMinutes(0);

        Set<ConstraintViolation<TimeEntryForm>> violations = validator.validate(form);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("durationMinutes"));
    }

    @Test
    void requiresStreamAndTask() {
        TimeEntryForm form = new TimeEntryForm();
        Set<ConstraintViolation<TimeEntryForm>> violations = validator.validate(form);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(3);
    }

    private TimeEntryForm baseForm() {
        TimeEntryForm form = new TimeEntryForm();
        form.setStreamId(UUID.randomUUID());
        form.setTaskName("Task");
        form.setEntryDate(LocalDate.now());
        form.setDurationMinutes(10);
        return form;
    }
}

