package com.nobelscript.rockpapersscissors.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameSession {

    private String sessionId;
    private int totalRounds;
    private int wins;
    private int losses;
    private int ties;

    public GameSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public void addRound(Result result) {
        totalRounds++;
        switch (result) {
            case WIN -> wins++;
            case LOSE -> losses++;
            case TIE -> ties++;
        }
    }
}

