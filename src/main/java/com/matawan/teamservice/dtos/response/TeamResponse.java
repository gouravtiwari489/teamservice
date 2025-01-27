package com.matawan.teamservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for returning team details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {

    private Long id;

    private String name;

    private List<PlayerResponse> players;

    private String acronym;

    private double budget;


}
