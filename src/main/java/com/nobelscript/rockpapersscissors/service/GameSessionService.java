package com.nobelscript.rockpapersscissors.service;

import com.nobelscript.rockpapersscissors.model.GameSession;
import com.nobelscript.rockpapersscissors.model.Move;
import com.nobelscript.rockpapersscissors.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameSessionService {

    private static final Logger logger = LoggerFactory.getLogger(GameSessionService.class);

    // we can use persistent storage to store the game sessions - I used this approach for simplicity
    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();

    /**
     * Generates a new game session with a unique session ID, logs the session start,
     * adds the session to the sessions map, and returns the session ID.
     *
     * @return The unique session ID of the newly created game session.
     */
    public String startNewSession() {
        String sessionId = UUID.randomUUID().toString();
        logger.info("New game session started with sessionId: {}", sessionId);
        sessions.put(sessionId, new GameSession(sessionId));
        return sessionId;
    }

    /**
     * Removes the specified game session identified by the given sessionId.
     *
     * @param sessionId The unique identifier of the game session to be terminated.
     */
    public void terminateSession(String sessionId) {
        sessions.remove(sessionId);
        logger.info("Game session terminated with sessionId: {}", sessionId);
    }

    /**
     * Retrieves the statistics of a specific game session identified by the given sessionId.
     *
     * @param sessionId The unique identifier of the game session to retrieve statistics for.
     * @return The GameSession object containing the statistics for the specified session.
     */
    public GameSession getSessionState(String sessionId) {
        logger.debug("Retrieved session stats for sessionId: {}", sessionId);
        return sessions.get(sessionId);
    }

    /**
     * Updates the game session with the specified moves and result.
     *
     * @param sessionId The unique identifier of the game session to update.
     * @param playerMove The move made by the player.
     * @param computerMove The move generated by the computer.
     * @param result The result of the game session based on the moves.
     */
    public void updateSession(String sessionId, Move playerMove, Move computerMove, Result result) {
        GameSession session = sessions.get(sessionId);
        if (session == null) {
            logger.warn("Session not found for sessionId: {}", sessionId);
            return;
        }
        logger.debug("Updated session stats for sessionId: {}, playerMove: {}, computerMove: {}, result: {}",
                sessionId, playerMove, computerMove, result);
        session.addRound(result);
    }
}


