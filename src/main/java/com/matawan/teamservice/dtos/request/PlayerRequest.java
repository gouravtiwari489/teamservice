package com.matawan.teamservice.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Request DTO for Player.
 */
@Data
@Builder
public class PlayerRequest {

    @NotEmpty(message = "Player name cannot be empty")
    private String name;

    @NotEmpty(message = "Player position cannot be empty")
    private String position;

}
