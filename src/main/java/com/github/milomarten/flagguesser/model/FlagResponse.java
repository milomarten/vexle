package com.github.milomarten.flagguesser.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class FlagResponse {
    private FlagComparison comparison;
    private List<IndividualFlagResult> individualFlagResults;
}
