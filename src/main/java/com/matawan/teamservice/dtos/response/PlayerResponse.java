package com.matawan.teamservice.dtos.response;

import lombok.Builder;
import lombok.Data;

/**
 * Response DTO for Player.
 */
@Data
@Builder
public class PlayerResponse {

    private Long id;
    private String name;
    private String position;
}
