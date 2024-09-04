package com.nobelscript.rockpapersscissors.model;

public enum Move {
    ROCK, PAPER, SCISSORS;

    public Result compareMoves(Move otherMove) {
        if (this == otherMove) {
            return Result.TIE;
        }
        return switch (this) {
            case ROCK -> (otherMove == SCISSORS) ? Result.WIN : Result.LOSE;
            case PAPER -> (otherMove == ROCK) ? Result.WIN : Result.LOSE;
            case SCISSORS -> (otherMove == PAPER) ? Result.WIN : Result.LOSE;
        };
    }

    public static Move randomMove() {
        Move[] moves = values();
        return moves[(int) (Math.random() * moves.length)];
    }
}

