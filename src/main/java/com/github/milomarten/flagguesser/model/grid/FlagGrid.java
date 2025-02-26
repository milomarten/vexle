package com.github.milomarten.flagguesser.model.grid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.milomarten.flagguesser.model.*;
import com.github.milomarten.flagguesser.service.FlagGridService;
import lombok.Data;

import java.util.*;

@Data
public class FlagGrid {
    private final Map<FlagColor, Presence> colors = new EnumMap<>(FlagColor.class);
    private final Map<FlagCharge, YesCounts> charges = new EnumMap<>(FlagCharge.class);
    private final Map<FlagPattern, YesCounts> patterns = new EnumMap<>(FlagPattern.class);

    private boolean allColorsFound = false;
    private boolean allChargesFound = false;
    private boolean allPatternsFound = false;

    public void push(IndividualFlagResult guess) {
        guess.getColors().forEach((color, isPresent) -> {
            this.colors.put(color,isPresent ? Presence.PRESENT : Presence.ABSENT);
        });

        guess.getPatterns().forEach((pattern, presence) -> {
            this.patterns.merge(pattern, presence.toYesCounts(), FlagGrid::max);
        });

        guess.getCharges().forEach((charge, presence) -> {
            this.charges.merge(charge, presence.toYesCounts(), FlagGrid::max);
        });
    }

    public void forAnswer(Flag answerFlag) {
        this.allColorsFound = answerFlag.getColors()
                .stream()
                .allMatch(fc -> colors.get(fc) == Presence.PRESENT);

        if (answerFlag.getPatterns() == null) {
            this.allPatternsFound = true;
        } else {
            this.allPatternsFound = answerFlag.getPatterns()
                    .stream()
                    .allMatch(fp -> patterns.get(fp).isAllFound());
        }

        if (answerFlag.getCharges() == null) {
            this.allChargesFound = true;
        } else {
            this.allChargesFound = answerFlag.getCharges()
                    .stream()
                    .allMatch(fc -> charges.get(fc).isAllFound());
        }
    }

    private static YesCounts max(YesCounts one, YesCounts two) {
        if (one.isAllFound() && !two.isAllFound()) {
            return one;
        } else if (!one.isAllFound() && two.isAllFound()) {
            return two;
        } else {
            return Collections.max(List.of(one, two), Comparator.comparing(YesCounts::getPresent));
        }
    }
}
