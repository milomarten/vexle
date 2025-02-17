package com.github.milomarten.flagguesser.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlagView {
    private final String code;
    private final String name;
    private final String emoji;

    public FlagView(Flag answerFlag) {
        this(answerFlag.getCountryCode(), answerFlag.getName(), answerFlag.getEmoji());
    }
}
