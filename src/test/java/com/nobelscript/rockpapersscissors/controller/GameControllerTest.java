package com.nobelscript.rockpapersscissors.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nobelscript.rockpapersscissors.model.GameSession;
import com.nobelscript.rockpapersscissors.service.GameService;
import com.nobelscript.rockpapersscissors.service.GameSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private GameService gameService;

    @MockBean
    private GameSessionService gameSessionService;

    private String sessionId;
    private ObjectMapper objectMapper = new ObjectMapper();

    private GameSession gameSession;

    @BeforeEach
    public void setUp() {
        sessionId = "test-session-id";
        gameSession = new GameSession();
        gameSession.setTotalRounds(10);
        gameSession.setWins(5);
        gameSession.setLosses(3);
        gameSession.setTies(2);

        // Mock the behavior
        when(gameSessionService.getSessionState(sessionId)).thenReturn(gameSession);
    }

    @Test
    public void testPlayGameAfterTermination() throws Exception {

        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/game/play/{sessionId}", "4668")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"playerMove\":\"ROCK\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map responseMap = objectMapper.readValue(responseString, Map.class);

        // Verify the response map
        assertEquals("Invalid Session", responseMap.get("message"));
    }

    @Test
    public void testStartGame() throws Exception {
        // Mock the service to return a specific session ID

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/game/start") // Ensure path matches controller
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testTerminateGame() throws Exception {
        // Mock the service method to ensure it gets called correctly
        doNothing().when(gameSessionService).terminateSession(sessionId);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/game/terminate/{sessionId}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Game session " + sessionId + " terminated."));

        // Verify that the terminateSession method was called with the correct sessionId
        verify(gameSessionService, times(1)).terminateSession(sessionId);
    }


    @Test
    public void testGetGameStats() throws Exception {
        // Mock the service method to return the predefined GameSession
        when(gameSessionService.getSessionState(sessionId)).thenReturn(gameSession);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/game/stats/{sessionId}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalRounds").value(gameSession.getTotalRounds()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wins").value(gameSession.getWins()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.losses").value(gameSession.getLosses()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ties").value(gameSession.getTies()));
    }
}
