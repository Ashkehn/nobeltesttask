package com.nobelscript.rockpapersscissors.dto;

import com.nobelscript.rockpapersscissors.model.Move;
import com.nobelscript.rockpapersscissors.model.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GameResponse {
    private Move playerMove;
    private Move computerMove;
    private Result result;
}
