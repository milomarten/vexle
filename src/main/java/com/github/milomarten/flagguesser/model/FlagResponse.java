package com.github.milomarten.flagguesser.model;

import com.github.milomarten.flagguesser.model.grid.FlagGrid;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class FlagResponse {
    private FlagGrid comparison;
    private List<IndividualFlagResult> individualFlagResults;
    private GameStatus status;
    private FlagView answer;
}
