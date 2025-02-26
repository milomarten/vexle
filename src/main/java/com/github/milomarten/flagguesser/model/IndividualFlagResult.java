package com.github.milomarten.flagguesser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.EnumMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class IndividualFlagResult extends FlagView {
    @Setter private Integer distance;
    private final Map<FlagColor, Boolean> colors = new EnumMap<>(FlagColor.class);
    private final Map<FlagCharge, YesNoCounts> charges = new EnumMap<>(FlagCharge.class);
    private final Map<FlagPattern, YesNoCounts> patterns = new EnumMap<>(FlagPattern.class);

    public IndividualFlagResult(Flag flag) {
        super(flag.getCountryCode(), flag.getName(), flag.getEmoji());
    }
}
