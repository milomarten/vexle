package com.github.milomarten.flagguesser.model;

import com.github.milomarten.flagguesser.util.MultiEnumSet;
import lombok.Data;
import org.apache.commons.collections4.MultiSet;

import java.util.EnumSet;
import java.util.Set;

@Data
public class FlagComparison {
    private final Set<FlagColor> presentColors = EnumSet.noneOf(FlagColor.class);
    private final Set<FlagColor> absentColors = EnumSet.noneOf(FlagColor.class);

    private final MultiSet<FlagCharge> presentCharges = new MultiEnumSet<>(FlagCharge.class);
    private final MultiSet<FlagCharge> absentCharges = new MultiEnumSet<>(FlagCharge.class);

    private final MultiSet<FlagPattern> presentPatterns = new MultiEnumSet<>(FlagPattern.class);
    private final MultiSet<FlagPattern> absentPatterns = new MultiEnumSet<>(FlagPattern.class);

    public FlagComparison merge(FlagComparison other) {
        this.presentColors.addAll(other.presentColors);
        this.absentColors.addAll(other.absentColors);

        mergeMultisets(FlagCharge.values(), this.presentCharges, other.presentCharges);
        mergeMultisets(FlagCharge.values(), this.absentCharges, other.absentCharges);

        mergeMultisets(FlagPattern.values(), this.presentPatterns, other.presentPatterns);
        mergeMultisets(FlagPattern.values(), this.absentPatterns, other.absentPatterns);

        return this;
    }

    private static <T> void mergeMultisets(T[] universe, MultiSet<T> left, MultiSet<T> right) {
        for (var item : universe) {
            int newCount = Math.max(left.getCount(item), right.getCount(item));
            left.setCount(item, newCount);
        }
    }
}
