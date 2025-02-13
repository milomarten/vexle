package com.github.milomarten.flagguesser.service;

import com.github.milomarten.flagguesser.exception.UnknownFlagCodeException;
import com.github.milomarten.flagguesser.model.Flag;
import com.github.milomarten.flagguesser.model.FlagComparison;
import com.github.milomarten.flagguesser.model.FlagResponse;
import com.github.milomarten.flagguesser.model.IndividualFlagResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FlagCompareService {
    private final FlagLoader flagLoader;

    public FlagComparison compare(Flag guessFlag, Flag answerFlag) {
        var comparison = new FlagComparison();

        for (var attr : guessFlag.getColors()) {
            comparison.getColors().put(attr, answerFlag.getColors().contains(attr));
        }

        if (guessFlag.getPatterns() != null) {
            for (var attr : guessFlag.getPatterns().uniqueSet()) {
                var guessCount = guessFlag.getPatterns().getCount(attr);
                var answerCount = answerFlag.getPatterns().getCount(attr);

                var presentCount = Math.min(guessCount, answerCount);
                var absentCount = Math.max(guessCount - answerCount, 0);

                comparison.getPatterns().put(attr, new FlagComparison.Result(presentCount, absentCount, false));
            }
        }

        if (guessFlag.getCharges() != null) {
            for (var attr : guessFlag.getCharges().uniqueSet()) {
                var guessCount = guessFlag.getCharges().getCount(attr);
                var answerCount = answerFlag.getCharges().getCount(attr);

                var presentCount = Math.min(guessCount, answerCount);
                var absentCount = Math.max(guessCount - answerCount, 0);

                comparison.getCharges().put(attr, new FlagComparison.Result(presentCount, absentCount, false));
            }
        }

        return comparison;
    }

    public FlagResponse multiCompare(String answer, String... guesses) {
        var answerFlag = flagLoader.getFlag(answer).orElseThrow(() -> new UnknownFlagCodeException(answer));
        var guessFlags = Arrays.stream(guesses)
                .map(guess -> flagLoader.getFlag(guess)
                                .orElseThrow(() -> new UnknownFlagCodeException(guess)))
                .toList();

        var mergedComparison = guessFlags
                .stream()
                .map(guessFlag -> compare(guessFlag, answerFlag))
                .reduce(FlagComparison::merge)
                .orElseThrow();

        var allColors = answerFlag.getColors()
                .stream()
                .allMatch(fc -> mergedComparison.getColors().getOrDefault(fc, false));
        mergedComparison.setFoundAllColors(allColors);

        computePatterns(answerFlag, mergedComparison);
        computeCharges(answerFlag, mergedComparison);
        mergedComparison.setGuessedCorrectly(Arrays.asList(guesses).contains(answer));

        return new FlagResponse(mergedComparison, computeIndividualResults(guessFlags));
    }

    private static void computePatterns(Flag answerFlag, FlagComparison mergedComparison) {
        var allPatterns = answerFlag.getPatterns()
                .entrySet()
                .stream()
                .allMatch(answerPattern -> {
                    if(mergedComparison.getPatterns().containsKey(answerPattern.getElement())) {
                        var guessCount = mergedComparison.getPatterns().get(answerPattern.getElement());
                        if (answerPattern.getCount() == guessCount.getPresent()) {
                            guessCount.setFoundAll(true);
                            return true;
                        }
                    }
                    return false;
                });
        mergedComparison.setFoundAllPatterns(allPatterns || answerFlag.getPatterns().isEmpty());
    }

    private static void computeCharges(Flag answerFlag, FlagComparison mergedComparison) {
        var allCharges = answerFlag.getCharges()
                .entrySet()
                .stream()
                .allMatch(answerCharges -> {
                    if(mergedComparison.getCharges().containsKey(answerCharges.getElement())) {
                        var guessCount = mergedComparison.getCharges().get(answerCharges.getElement());
                        if (answerCharges.getCount() == guessCount.getPresent()) {
                            guessCount.setFoundAll(true);
                            return true;
                        }
                    }
                    return false;
                });
        mergedComparison.setFoundAllCharges(allCharges || answerFlag.getCharges().isEmpty());
    }

    private List<IndividualFlagResult> computeIndividualResults(List<Flag> guesses) {
        return guesses.stream()
                .map(IndividualFlagResult::new)
                .toList();
    }
}
