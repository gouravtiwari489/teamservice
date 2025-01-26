package com.matawan.teamservice.unit;

import com.matawan.teamservice.dtos.request.TeamRequest;
import com.matawan.teamservice.dtos.response.TeamResponse;
import com.matawan.teamservice.entity.Team;
import com.matawan.teamservice.exception.TeamNotFoundException;
import com.matawan.teamservice.repository.TeamRepository;
import com.matawan.teamservice.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceUnitTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    private TeamRequest teamRequest;

    private Team team;

    @BeforeEach
    public void setUp() {

        teamRequest = TeamRequest.builder()
                .name("Nice")
                .acronym("NC")
                .players(List.of())
                .budget(1000000.00)
                .build();

        team = Team.builder()
                .id(1L)
                .name("Nice")
                .acronym("NC")
                .players(List.of())
                .budget(1000000.00)
                .build();
    }

    @Test
    public void testCreateTeam() {
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        TeamResponse teamResponse = teamService.saveTeam(teamRequest);

        assertNotNull(teamResponse);
        assertEquals("Nice", teamResponse.getName());
        assertEquals("NC", teamResponse.getAcronym());
        assertEquals(1000000.00, teamResponse.getBudget());

        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    public void testUpdateTeam() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        TeamRequest updatedTeam = TeamRequest.builder()
                .name("Updated Team")
                .acronym("UT")
                .budget(2000000.00)
                .players(List.of())
                .build();

        TeamResponse teamResponse = teamService.updateTeam(1L, updatedTeam);

        assertEquals("Updated Team", teamResponse.getName());
        assertEquals("UT", teamResponse.getAcronym());
        assertEquals(2000000.00, teamResponse.getBudget());

        verify(teamRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    public void testGetTeam() {
        when(teamRepository.findById(1L)).thenReturn(java.util.Optional.of(team));

        TeamResponse foundTeam = teamService.getTeamById(1L);

        assertNotNull(foundTeam);
        assertEquals("Nice", foundTeam.getName());
        assertEquals("NC", foundTeam.getAcronym());

        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTeamNotFound() {
        when(teamRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        TeamNotFoundException thrown = assertThrows(TeamNotFoundException.class, () -> {
            teamService.getTeamById(1L);
        });

        assertEquals("Team not found by id: 1", thrown.getMessage());

        verify(teamRepository, times(1)).findById(1L);
    }
}
