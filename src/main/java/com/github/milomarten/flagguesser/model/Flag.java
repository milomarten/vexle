package com.github.milomarten.flagguesser.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.milomarten.flagguesser.util.MultiEnumSetDeserializer;
import lombok.Data;
import org.apache.commons.collections4.MultiSet;

import java.util.EnumSet;
import java.util.Set;

@Data
public class Flag {
    private String countryCode;
    private String name;
    private String emoji;
    @JsonDeserialize(as = EnumSet.class)
    private Set<FlagColor> colors;
    @JsonDeserialize(using = MultiEnumSetDeserializer.class)
    private MultiSet<FlagCharge> charges;
    @JsonDeserialize(using = MultiEnumSetDeserializer.class)
    private MultiSet<FlagPattern> patterns;
}
