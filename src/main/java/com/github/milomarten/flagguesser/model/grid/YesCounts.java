package com.github.milomarten.flagguesser.model.grid;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class YesCounts {
    public static final YesCounts NOOP = new YesCounts(0, false);

    private int present;
    private boolean allFound;

    @JsonValue
    public String asGameString() {
        if (present == 0) {
            return allFound ? "X" : "??";
        } else {
            return allFound ? String.valueOf(present) : (">" + present);
        }
    }
}
