package com.matawan.teamservice.controller;

import com.matawan.teamservice.dtos.request.TeamRequest;
import com.matawan.teamservice.dtos.response.TeamResponse;
import com.matawan.teamservice.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Controller for handling CRUD operations related to teams.
 */
@RestController
@RequestMapping("/teams")
@Validated
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * Creates a new team
     *
     * @param teamRequest The team request object to be created.
     * @return A ResponseEntity containing the created team with HTTP status 201 Created.
     */
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamRequest teamRequest) {
        TeamResponse createdTeam = teamService.saveTeam(teamRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    /**
     * Updates an existing team
     *
     * @param id The ID of the team to update.
     * @param updatedTeam The updated team data.
     * @return A ResponseEntity containing the updated team with HTTP status 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamRequest updatedTeam) {
        TeamResponse team = teamService.updateTeam(id, updatedTeam);
        return ResponseEntity.ok(team);
    }

    /**
     * Retrieves a team by its ID
     *
     * @param id The ID of the team to retrieve.
     * @return A ResponseEntity containing the team with HTTP status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable @NotNull Long id) {
        TeamResponse team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    /**
     * Deletes a team by its ID
     *
     * @param id The ID of the team to delete
     * @return A ResponseEntity with HTTP status 204 No Content to indicate successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<TeamResponse>> getTeams(Pageable pageable) {
        Page<TeamResponse> teamResponse = teamService.getTeams(pageable);
        return ResponseEntity.ok(teamResponse);
    }
}
