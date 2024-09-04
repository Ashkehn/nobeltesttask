package com.nobelscript.rockpapersscissors.dto;

import com.nobelscript.rockpapersscissors.model.Move;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameRequest {
    private Move playerMove;
}
