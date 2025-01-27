package com.matawan.teamservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matawan.teamservice.dtos.response.TeamResponse;
import com.matawan.teamservice.entity.Player;
import com.matawan.teamservice.entity.Team;
import com.matawan.teamservice.integration.deserialiser.PageResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeamsPaginatedGetAllIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    public void setup() {
        teamRepository.deleteAll();

        // Create and save teams with players
        List<Team> teams = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            List<Player> players = new ArrayList<>();
            for (int j = 1; j <= 2; j++) {
                players.add(Player.builder()
                        .name("Player " + i + "-" + j) // Unique player names
                        .position("Position " + j)
                        .build());
            }
            Team team = Team.builder()
                    .name("Team " + i)
                    .acronym("T" + i)
                    .budget(100000 * i)
                    .players(players)
                    .build();

            players.forEach(player -> player.setTeam(team));

            teams.add(team);
        }

        teamRepository.saveAll(teams);
    }

    @AfterEach
    public void cleanup() {
        teamRepository.deleteAll();
    }

    @Test
    public void testGetTeamsWithPagination() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/teams")
                        .param("page", "0") // First page
                        .param("size", "2")  // Page size of 2
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println(responseContent);

        PageResponse<TeamResponse> pageResponse = objectMapper.readValue(
                responseContent,
                new com.fasterxml.jackson.core.type.TypeReference<>() {}
        );


        assertThat(pageResponse.getContent().size()).isEqualTo(2);
        assertThat(pageResponse.getContent().get(0).getPlayers().size()).isEqualTo(2);

        TeamResponse firstTeam = pageResponse.getContent().get(0);
        assertThat(firstTeam.getName()).isEqualTo("Team 1");
        assertThat(firstTeam.getPlayers().get(0).getName()).isEqualTo("Player 1-1");
    }


}
