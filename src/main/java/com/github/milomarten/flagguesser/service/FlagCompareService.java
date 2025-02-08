package com.github.milomarten.flagguesser.service;

import com.github.milomarten.flagguesser.exception.UnknownFlagCodeException;
import com.github.milomarten.flagguesser.model.FlagComparison;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FlagCompareService {
    private final FlagLoader flagLoader;

    public FlagComparison compare(String guess, String answer) {
        var comparison = new FlagComparison();
        var guessFlag = flagLoader.getFlag(guess).orElseThrow(() -> new UnknownFlagCodeException(guess));
        var answerFlag = flagLoader.getFlag(answer).orElseThrow(() -> new UnknownFlagCodeException(answer));

        for (var attr : guessFlag.getColors()) {
            if (answerFlag.getColors().contains(attr)) {
                comparison.getPresentColors().add(attr);
            } else {
                comparison.getAbsentColors().add(attr);
            }
        }

        for (var attr : guessFlag.getPatterns().uniqueSet()) {
            var guessCount = guessFlag.getPatterns().getCount(attr);
            var answerCount = answerFlag.getPatterns().getCount(attr);

            var presentCount = Math.min(guessCount, answerCount);
            var absentCount = Math.max(guessCount - answerCount, 0);

            comparison.getPresentPatterns().setCount(attr, presentCount);
            comparison.getAbsentPatterns().setCount(attr, absentCount);
        }

        for (var attr : guessFlag.getCharges().uniqueSet()) {
            var guessCount = guessFlag.getCharges().getCount(attr);
            var answerCount = answerFlag.getCharges().getCount(attr);

            var presentCount = Math.min(guessCount, answerCount);
            var absentCount = Math.max(guessCount - answerCount, 0);

            comparison.getPresentCharges().setCount(attr, presentCount);
            comparison.getAbsentCharges().setCount(attr, absentCount);
        }

        return comparison;
    }

    public FlagComparison multiCompare(String answer, String... guesses) {
        return Stream.of(guesses)
                .map(guess -> compare(guess, answer))
                .reduce(new FlagComparison(), FlagComparison::merge);
    }
}
