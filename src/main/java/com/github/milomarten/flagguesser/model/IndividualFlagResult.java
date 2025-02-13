package com.github.milomarten.flagguesser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class IndividualFlagResult extends FlagView {
    private Integer distance;

    public IndividualFlagResult(Flag flag) {
        super(flag.getCountryCode(), flag.getName(), flag.getEmoji());
    }
}
