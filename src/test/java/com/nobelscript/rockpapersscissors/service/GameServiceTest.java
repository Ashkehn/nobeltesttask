package com.nobelscript.rockpapersscissors.service;


import com.nobelscript.rockpapersscissors.dto.GameResponse;
import com.nobelscript.rockpapersscissors.model.GameSession;
import com.nobelscript.rockpapersscissors.model.Move;
import com.nobelscript.rockpapersscissors.model.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameServiceTest {


    @Test
    @DisplayName("Valid session and player move")
    public void test_valid_session_and_player_move() {
        // Arrange
        GameSessionService gameSessionService = mock(GameSessionService.class);
        GameService gameService = new GameService(gameSessionService);
        String sessionId = "valid-session-id";
        Move playerMove = Move.ROCK;
        GameSession gameSession = new GameSession(sessionId);

        when(gameSessionService.getSessionState(sessionId)).thenReturn(gameSession);
        doNothing().when(gameSessionService).updateSession(anyString(), any(Move.class), any(Move.class), any(Result.class));

        // Act
        GameResponse response = gameService.playGame(sessionId, playerMove);

        // Assert
        assertNotNull(response);
        assertEquals(playerMove, response.getPlayerMove());
        assertNotNull(response.getComputerMove());
        assertNotNull(response.getResult());
    }

    @Test
    @DisplayName("Invalid session ID throws an exception")
    public void test_invalid_session_throws_exception() {
        // Arrange
        GameSessionService gameSessionService = mock(GameSessionService.class);
        GameService gameService = new GameService(gameSessionService);
        String invalidSessionId = "invalid-session-id";
        Move playerMove = Move.PAPER;

        when(gameSessionService.getSessionState(invalidSessionId)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            gameService.playGame(invalidSessionId, playerMove);
        });
    }
}
