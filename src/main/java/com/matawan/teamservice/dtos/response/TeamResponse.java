package com.matawan.teamservice.dtos.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * Response DTO for returning team details.
 */
@Data
@Builder
public class TeamResponse {

    private Long id;

    private String name;

    private List<PlayerResponse> players;

    private String acronym;

    private double budget;


}
