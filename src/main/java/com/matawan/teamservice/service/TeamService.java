package com.matawan.teamservice.service;

import com.matawan.teamservice.dtos.request.PlayerRequest;
import com.matawan.teamservice.dtos.request.TeamRequest;
import com.matawan.teamservice.dtos.response.PlayerResponse;
import com.matawan.teamservice.dtos.response.TeamResponse;
import com.matawan.teamservice.entity.Player;
import com.matawan.teamservice.entity.Team;
import com.matawan.teamservice.exception.TeamNotFoundException;
import com.matawan.teamservice.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public TeamResponse saveTeam(TeamRequest teamRequest) {
        Team team = convertToEntity(teamRequest);
        team.getPlayers().forEach(player -> player.setTeam(team));
        // Save team
        Team savedTeam = teamRepository.save(team);
        return convertToDto(savedTeam);
    }

    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TeamResponse getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team not found by id: "+id));
        return convertToDto(team);
    }

    @Transactional
    public TeamResponse updateTeam(Long id, TeamRequest updatedTeamRequest) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Update team fields
        existingTeam.setName(updatedTeamRequest.getName());
        existingTeam.setAcronym(updatedTeamRequest.getAcronym());
        existingTeam.setBudget(updatedTeamRequest.getBudget());

        // Update players
        existingTeam.getPlayers().clear();
        List<Player> updatedPlayers = convertPlayerRequestsToPlayers(updatedTeamRequest.getPlayers());
        updatedPlayers.forEach(player -> player.setTeam(existingTeam));
        existingTeam.getPlayers().addAll(updatedPlayers);

        Team updatedTeam = teamRepository.save(existingTeam);
        return convertToDto(updatedTeam);
    }

    @Transactional
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new TeamNotFoundException("Team not found by id: "+id);
        }
        teamRepository.deleteById(id);
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
