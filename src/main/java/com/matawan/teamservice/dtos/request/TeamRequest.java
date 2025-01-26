package com.matawan.teamservice.dtos.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Request DTO for creating or updating a team.
 */
@Data
@Builder
public class TeamRequest {

    @NotEmpty(message = "Team name cannot be empty or null")
    private String name;

    @NotNull(message = "Team Players list cannot be null")
    private List<PlayerRequest> players;

    @NotEmpty(message = "Team acronym cannot be null or empty")
    private String acronym;

    @NotNull(message = "Team budget cannot be null")
    @Positive(message = "Team budget must be a positive number")
    private Double budget;

}