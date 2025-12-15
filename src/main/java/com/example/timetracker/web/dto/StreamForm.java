package com.example.timetracker.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StreamForm {
    @NotBlank(message = "Stream name is required")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    @Size(max = 200, message = "Description must be at most 200 characters")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

