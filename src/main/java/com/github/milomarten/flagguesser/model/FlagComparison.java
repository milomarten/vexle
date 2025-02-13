package com.github.milomarten.flagguesser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

@Data
public class FlagComparison {
    private final Map<FlagColor, Boolean> colors = new EnumMap<>(FlagColor.class);
    private boolean foundAllColors = false;

    private final Map<FlagCharge, Result> charges = new EnumMap<>(FlagCharge.class);
    private boolean foundAllCharges = false;

    private final Map<FlagPattern, Result> patterns = new EnumMap<>(FlagPattern.class);
    private boolean foundAllPatterns = false;

    @JsonProperty
    public boolean foundAll() { return foundAllColors && foundAllCharges && foundAllPatterns; }

    public FlagComparison merge(FlagComparison other) {
        var newComparison = new FlagComparison();
        newComparison.colors.putAll(this.colors);
        other.colors.forEach((color, has) -> {
            newComparison.colors.merge(color, has, (a, b) -> a || b);
        });
        newComparison.foundAllColors = this.foundAllColors || other.foundAllColors;

        mergeMaps(this.charges, other.charges, newComparison.charges);
        newComparison.foundAllCharges = this.foundAllCharges || other.foundAllCharges;
        mergeMaps(this.patterns, other.patterns, newComparison.patterns);
        newComparison.foundAllPatterns = this.foundAllPatterns || other.foundAllPatterns;

        return newComparison;
    }

    private static <T> void mergeMaps(Map<T, Result> left, Map<T, Result> right, Map<T, Result> result) {
        result.putAll(left);
        right.forEach((key, value) -> {
            result.merge(key, value, Result::merge);
        });
    }

    @Data
    @AllArgsConstructor
    public static class Result {
        public static final Result NOOP = new Result(0, 0, false);

        private int present;
        private int absent;
        private boolean foundAll;

        private Result merge(Result other) {
            return new Result(
                    Math.max(present, other.present),
                    Math.max(absent, other.absent),
                    foundAll || other.foundAll
            );
        }
    }
}
