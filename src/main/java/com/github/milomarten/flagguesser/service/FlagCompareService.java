package com.github.milomarten.flagguesser.service;

import com.github.milomarten.flagguesser.exception.UnknownFlagCodeException;
import com.github.milomarten.flagguesser.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlagCompareService {
    @Value("${maxGuesses: 6}")
    private int maxGuesses;

    private final FlagLoader flagLoader;

    private final FlagGridService flagGridService;

    public FlagResponse compare(List<String> guessFlagCodes, String answerFlagCode) {
        var answerFlag = flagLoader.getFlag(answerFlagCode)
                .orElseThrow(() -> new UnknownFlagCodeException(answerFlagCode));
        var guesses = guessFlagCodes.stream()
                .map(guess -> flagLoader.getFlag(guess)
                                .orElseThrow(() -> new UnknownFlagCodeException(guess)))
                .map(guess -> compare(guess, answerFlag))
                .toList();

        var response = new FlagResponse(flagGridService.createGrid(answerFlag, guesses),
                guesses, GameStatus.PLAYING, null);
        if (guessFlagCodes.contains(answerFlagCode)) {
            response.setStatus(GameStatus.WON);
            response.setAnswer(new FlagView(answerFlag));
        } else if (guesses.size() >= maxGuesses) {
            response.setStatus(GameStatus.LOST);
            response.setAnswer(new FlagView(answerFlag));
        }

        return response;
    }

    public IndividualFlagResult compare(Flag guessFlag, Flag answerFlag) {
        var result = new IndividualFlagResult(guessFlag);

        for (var attr : guessFlag.getColors()) {
            result.getColors().put(attr, answerFlag.getColors().contains(attr));
        }

        if (guessFlag.getPatterns() != null) {
            for (var attr : guessFlag.getPatterns().uniqueSet()) {
                var guessCount = guessFlag.getPatterns().getCount(attr);
                var answerCount = answerFlag.getPatterns() == null ?
                        0 : answerFlag.getPatterns().getCount(attr);

                var presentCount = Math.min(guessCount, answerCount);
                var absentCount = Math.max(guessCount - answerCount, 0);

                result.getPatterns().put(attr, new YesNoCounts(presentCount, absentCount, guessCount >= answerCount));
            }
        }

        if (guessFlag.getCharges() != null) {
            for (var attr : guessFlag.getCharges().uniqueSet()) {
                var guessCount = guessFlag.getCharges().getCount(attr);
                var answerCount = answerFlag.getCharges() == null ?
                        0 : answerFlag.getCharges().getCount(attr);

                var presentCount = Math.min(guessCount, answerCount);
                var absentCount = Math.max(guessCount - answerCount, 0);

                result.getCharges().put(attr, new YesNoCounts(presentCount, absentCount, guessCount >= answerCount));
            }
        }

        return result;
    }
}
