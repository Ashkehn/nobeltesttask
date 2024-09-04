package com.nobelscript.rockpapersscissors.service;

import com.nobelscript.rockpapersscissors.model.GameSession;
import com.nobelscript.rockpapersscissors.model.Move;
import com.nobelscript.rockpapersscissors.model.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GameSessionServiceTest {

    @Spy
    GameSessionService service;

    @Test
    @DisplayName("Starting a new game session returns a unique session ID")
    public void test_start_new_session_returns_unique_id() {
        String sessionId1 = service.startNewSession();
        String sessionId2 = service.startNewSession();

        assertNotNull(sessionId1);
        assertNotNull(sessionId2);
        assertNotEquals(sessionId1, sessionId2);
    }

    @Test
    @DisplayName("Terminating a non-existent session does not throw an error")
    public void test_terminate_non_existent_session() {
        assertDoesNotThrow(() -> service.terminateSession("non-existent-session-id"));
    }

    @Test
    @DisplayName("Retrieving session stats returns the correct GameSession object")
    public void test_retrieving_session_stats_returns_correct_object() {
        // Given
        String sessionId = service.startNewSession();

        // When
        service.updateSession(sessionId, Move.ROCK, Move.ROCK, Result.TIE);
        GameSession session = service.getSessionState(sessionId);

        // Then
        assertNotNull(session);
        assertEquals(sessionId, session.getSessionId());
        assertEquals(1, session.getTotalRounds());
        assertEquals(0, session.getWins());
        assertEquals(0, session.getLosses());
        assertEquals(1, session.getTies());
    }
}