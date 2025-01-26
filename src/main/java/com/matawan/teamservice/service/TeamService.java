package com.matawan.teamservice.service;

import com.matawan.teamservice.dtos.request.PlayerRequest;
import com.matawan.teamservice.dtos.request.TeamRequest;
import com.matawan.teamservice.dtos.response.PlayerResponse;
import com.matawan.teamservice.dtos.response.TeamResponse;
import com.matawan.teamservice.entity.Player;
import com.matawan.teamservice.entity.Team;
import com.matawan.teamservice.exception.TeamNotFoundException;
import com.matawan.teamservice.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Transactional
    public TeamResponse saveTeam(TeamRequest teamRequest) {
        log.info("Saving a new team with name: {}", teamRequest.getName());
        Team team = convertToEntity(teamRequest);
        team.getPlayers().forEach(player -> player.setTeam(team));
        Team savedTeam = teamRepository.save(team);
        log.info("Team saved successfully with ID: {}", savedTeam.getId());
        return convertToDto(savedTeam);
    }

    @Transactional
    public TeamResponse updateTeam(Long id, TeamRequest updatedTeamRequest) {
        log.info("Updating team with ID: {}", id);

        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Team not found with ID: {}", id);
                    return new TeamNotFoundException("Team not found by id: " + id);
                });


        log.debug("Existing team found: {}", existingTeam);
        existingTeam.setName(updatedTeamRequest.getName());
        existingTeam.setAcronym(updatedTeamRequest.getAcronym());
        existingTeam.setBudget(updatedTeamRequest.getBudget());

        List<Player> existingPlayers = existingTeam.getPlayers();

        Iterator<Player> iterator = existingPlayers.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            player.setTeam(null);
            iterator.remove();
        }

        List<Player> updatedPlayers = convertPlayerRequestsToPlayers(updatedTeamRequest.getPlayers());
        updatedPlayers.forEach(player -> {
            player.setTeam(existingTeam);
            existingPlayers.add(player);
        });

        Team updatedTeam = teamRepository.save(existingTeam);
        log.info("Team updated successfully with ID: {}", updatedTeam.getId());
        return convertToDto(updatedTeam);
    }

    public List<TeamResponse> getAllTeams() {
        log.info("Fetching all teams");
        List<TeamResponse> teams = teamRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        log.info("Total teams found: {}", teams.size());
        return teams;
    }

    public TeamResponse getTeamById(Long id) {
        log.info("Fetching team with ID: {}", id);
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team not found by id: "+id));
        log.info("Team found: {}", team);
        return convertToDto(team);
    }

    @Transactional
    public void deleteTeam(Long id) {
        log.info("Deleting team with ID: {}", id);
        if (!teamRepository.existsById(id)) {
            throw new TeamNotFoundException("Team not found by id: "+id);
        }
        teamRepository.deleteById(id);
        log.info("Team deleted successfully with ID: {}", id);
    }

    private TeamResponse convertToDto(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .acronym(team.getAcronym())
                .budget(team.getBudget())
                .players(team.getPlayers().stream()
                        .map(player -> PlayerResponse.builder()
                                .id(player.getId())
                                .name(player.getName())
                                .position(player.getPosition())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private Team convertToEntity(TeamRequest teamRequest) {
        return Team.builder()
                .name(teamRequest.getName())
                .acronym(teamRequest.getAcronym())
                .budget(teamRequest.getBudget())
                .players(convertPlayerRequestsToPlayers(teamRequest.getPlayers()))
                .build();
    }

    private List<Player> convertPlayerRequestsToPlayers(List<PlayerRequest> playerRequests) {
        return playerRequests.stream()
                .map(playerRequest -> Player.builder()
                        .name(playerRequest.getName())
                        .position(playerRequest.getPosition())
                        .build())
                .collect(Collectors.toList());
    }
}
