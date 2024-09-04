package com.nobelscript.rockpapersscissors.service;

import com.nobelscript.rockpapersscissors.dto.GameResponse;
import com.nobelscript.rockpapersscissors.model.GameSession;
import com.nobelscript.rockpapersscissors.model.Move;
import com.nobelscript.rockpapersscissors.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameSessionService gameSessionService;

    public GameService(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    /**
     * Plays a game of rock-paper-scissors for the given session ID and player move.
     * Generates a random computer move, compares it with the player move, and updates the game session with the result.
     *
     * @param sessionId The ID of the game session.
     * @param playerMove The move chosen by the player.
     * @return The response containing the player's move, computer's move, and the result of the game.

     */
    public GameResponse playGame(String sessionId, Move playerMove) {
        logger.debug("Starting game for sessionId: {}, playerMove: {}", sessionId, playerMove);

        GameSession session = gameSessionService.getSessionState(sessionId);

        if (session == null) {
            logger.warn("Attempt to play with an invalid or terminated sessionId: {}", sessionId);

            throw new IllegalStateException("Invalid or terminated sessionId: " + sessionId);
        }

        Move computerMove = Move.randomMove();
        Result result = playerMove.compareMoves(computerMove);

        logger.info("Player move: {}, Computer move: {}, Result: {}", playerMove, computerMove, result);

        gameSessionService.updateSession(sessionId, playerMove, computerMove, result);

        return new GameResponse(playerMove, computerMove, result);
    }
}


