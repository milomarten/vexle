package com.github.milomarten.flagguesser.util;

import org.apache.commons.collections4.multiset.AbstractMapMultiSet;

import java.util.EnumMap;

public class MultiEnumSet<E extends Enum<E>> extends AbstractMapMultiSet<E> {
    public MultiEnumSet(Class<E> clazz) {
        super(new EnumMap<>(clazz));
    }
}
