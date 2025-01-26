package com.matawan.teamservice.integratiom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matawan.teamservice.dtos.request.PlayerRequest;
import com.matawan.teamservice.dtos.request.TeamRequest;
import com.matawan.teamservice.dtos.response.PlayerResponse;
import com.matawan.teamservice.dtos.response.TeamResponse;
import com.matawan.teamservice.entity.Team;
import com.matawan.teamservice.repository.TeamRepository;
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
public class TeamsApiIntegrationTest {

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

    @Test
    public void testCreateTeamWithEmptyPlayersList() throws Exception {

        TeamRequest teamRequest = TeamRequest.builder()
                .name("Nice")
                .acronym("NC")
                .players(List.of())
                .budget(1000000.00)
                .build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isCreated())  // Expect HTTP 201 Created
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        
        TeamResponse teamResponse = objectMapper.readValue(responseContent, TeamResponse.class);

        TeamResponse expectedResponse = TeamResponse.builder()
                .id(2L)
                .name("Nice")
                .acronym("NC")
                .players(List.of())
                .budget(1000000.00)
                .build();

        assertThat(teamResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    public void testCreateTeamWhenPlayersExist() throws Exception {

        TeamRequest teamRequest = TeamRequest.builder()
                .name("Nice")
                .acronym("NC")
                .players(List.of(PlayerRequest.builder().name("Gourav").position("MidFielder").build()))
                .budget(1000000.00)
                .build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isCreated())  // Expect HTTP 201 Created
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();

        TeamResponse teamResponse = objectMapper.readValue(responseContent, TeamResponse.class);

        TeamResponse expectedResponse = TeamResponse.builder()
                .id(2L)
                .name("Nice")
                .acronym("NC")
                .players(List.of(PlayerResponse.builder().id(1L).name("Gourav").position("MidFielder").build()))
                .budget(1000000.00)
                .build();

        assertThat(teamResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    public void testGetTeamByIdWhenValidId() throws Exception {
        Team team = teamRepository.findAll().getFirst();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/teams/{id}", team.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String getResponseContent = mvcResult.getResponse().getContentAsString();
        TeamResponse getTeamResponse = objectMapper.readValue(getResponseContent, TeamResponse.class);

        assertThat(getTeamResponse.getName()).isEqualTo("Nice");
        assertThat(getTeamResponse.getAcronym()).isEqualTo("NC");
    }


}
