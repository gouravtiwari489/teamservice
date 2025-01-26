package com.matawan.teamservice.controller;

import com.matawan.teamservice.dtos.request.TeamRequest;
import com.matawan.teamservice.dtos.response.TeamResponse;
import com.matawan.teamservice.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Controller for handling operations related to teams.
 * Provides CRUD operations for managing teams.
 */
@RestController
@RequestMapping("/teams")
@Validated
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * Creates a new team.
     *
     * @param teamRequest The team request object to be created.
     * @return A ResponseEntity containing the created team with HTTP status 201 Created.
     */
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamRequest teamRequest) {
        TeamResponse createdTeam = teamService.saveTeam(teamRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam); // Return HTTP 201 Created with the created team
    }

    /**
     * Updates an existing team.
     *
     * @param id The ID of the team to update.
     * @param updatedTeam The updated team data.
     * @return A ResponseEntity containing the updated team with HTTP status 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamRequest updatedTeam) {
        TeamResponse team = teamService.updateTeam(id, updatedTeam);
        return ResponseEntity.ok(team); // Return HTTP 200 OK with the updated team
    }

    /**
     * Retrieves all teams.
     *
     * @return A ResponseEntity containing the list of all teams with HTTP status 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        List<TeamResponse> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams); // Return HTTP 200 OK with the list of teams
    }

    /**
     * Retrieves a team by its ID.
     *
     * @param id The ID of the team to retrieve.
     * @return A ResponseEntity containing the team with HTTP status 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable @NotNull Long id) {
        TeamResponse team = teamService.getTeamById(id);
        return ResponseEntity.ok(team); // Return HTTP 200 OK with the team
    }

    /**
     * Deletes a team by its ID.
     *
     * @param id The ID of the team to delete.
     * @return A ResponseEntity with HTTP status 204 No Content to indicate successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build(); // Return HTTP 204 No Content
    }
}
