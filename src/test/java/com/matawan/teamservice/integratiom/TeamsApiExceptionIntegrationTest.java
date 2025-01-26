package com.matawan.teamservice.integratiom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matawan.teamservice.dtos.request.PlayerRequest;
import com.matawan.teamservice.dtos.request.TeamRequest;
import com.matawan.teamservice.dtos.response.PlayerResponse;
import com.matawan.teamservice.dtos.response.TeamResponse;
import com.matawan.teamservice.entity.Team;
import com.matawan.teamservice.repository.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeamsApiExceptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    public void setup() {

        Team team = Team.builder()
                .name("Nice")
                .acronym("NC")
                .players(new ArrayList<>())
                .budget(1000000.00)
                .build();
        teamRepository.save(team);
    }

    @AfterEach
    public void clear() {
        teamRepository.deleteAll();
    }

    @Test
    public void testCreateTeamWithInvalidRequest() throws Exception {

        TeamRequest teamRequest = TeamRequest.builder()
                .acronym("MS")
                .players(List.of())
                .budget(1000000.00)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testCreateTeamWithInvalidRequestAcronym() throws Exception {

        TeamRequest teamRequest = TeamRequest.builder()
                .name("Test")
                .players(List.of())
                .budget(1000000.00)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testCreateTeamWithInvalidRequestBudget() throws Exception {

        TeamRequest teamRequest = TeamRequest.builder()
                .name("Test")
                .acronym("")
                .players(List.of())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testCreateTeamWithInvalidRequestPlayers() throws Exception {

        TeamRequest teamRequest = TeamRequest.builder()
                .name("Test")
                .acronym("")
                .budget(10.0)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

    }


    @Test
    public void testGetTeamByIdWhenInvalidId() throws Exception {
        Team team = teamRepository.findAll().getFirst();

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/{id}", 369)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testDeleteTeamByIdWhenInvalidId() throws Exception {
        Team team = teamRepository.findAll().getFirst();

        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/{id}", team.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

    }

    @Test
    public void testUpdateTeamWhenIdInvalid() throws Exception {
        PlayerRequest player1 = PlayerRequest.builder()
                .name("Messi")
                .position("Forward")
                .build();

        PlayerRequest player2 = PlayerRequest.builder()
                .name("Drogba")
                .position("Midfielder")
                .build();

        TeamRequest teamRequest = TeamRequest.builder()
                .name("Nice")
                .acronym("NC")
                .players(List.of(player1, player2))
                .budget(1200000.00)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/369")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isNotFound())
                .andReturn();

    }


}
