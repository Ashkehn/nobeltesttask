package com.nobelscript.rockpapersscissors.controller;

import com.nobelscript.rockpapersscissors.dto.GameRequest;
import com.nobelscript.rockpapersscissors.dto.GameResponse;
import com.nobelscript.rockpapersscissors.model.ErrorResponse;
import com.nobelscript.rockpapersscissors.model.GameSession;
import com.nobelscript.rockpapersscissors.service.GameService;
import com.nobelscript.rockpapersscissors.service.GameSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final GameSessionService gameSessionService;

    public GameController(GameService gameService, GameSessionService gameSessionService) {
        this.gameService = gameService;
        this.gameSessionService = gameSessionService;
    }


    @PostMapping("/start")
    public ResponseEntity<String> startGame() {
        String sessionId = gameSessionService.startNewSession();
        return ResponseEntity.ok("Game started. Session ID: " + sessionId);
    }

    @PostMapping("/play/{sessionId}")
    public ResponseEntity<?> playGame(@PathVariable String sessionId, @RequestBody GameRequest request) {
        try {
            GameResponse response = gameService.playGame(sessionId, request.getPlayerMove());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            ErrorResponse errorResponse = new ErrorResponse("Invalid Session", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/terminate/{sessionId}")
    public ResponseEntity<String> terminateGame(@PathVariable String sessionId) {
        gameSessionService.terminateSession(sessionId);
        return ResponseEntity.ok("Game session " + sessionId + " terminated.");
    }

    @GetMapping("/stats/{sessionId}")
    public ResponseEntity<GameSession> getGameStats(@PathVariable String sessionId) {
        GameSession session = gameSessionService.getSessionState(sessionId);
        return ResponseEntity.ok(session);
    }
}
