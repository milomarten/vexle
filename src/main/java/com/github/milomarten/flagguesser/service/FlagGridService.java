package com.github.milomarten.flagguesser.service;

import com.github.milomarten.flagguesser.model.*;
import com.github.milomarten.flagguesser.model.grid.FlagGrid;
import com.github.milomarten.flagguesser.model.grid.Presence;
import com.github.milomarten.flagguesser.model.grid.YesCounts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlagGridService {
    public FlagGrid createGrid(Flag answer, List<IndividualFlagResult> guessResults) {
        var grid = new FlagGrid();

        for (var guess : guessResults) {
            grid.push(guess);
        }
        grid.forAnswer(answer);

        return populateTheRest(grid);
    }

    private FlagGrid populateTheRest(FlagGrid grid) {
        fill(grid.getColors(), FlagColor.values(), Presence.NOT_GUESSED);
        fill(grid.getPatterns(), FlagPattern.values(), YesCounts.NOOP);
        fill(grid.getCharges(), FlagCharge.values(), YesCounts.NOOP);

        return grid;
    }

    private static <E extends Enum<E>, V> void fill(Map<E, V> map, E[] universe, V value) {
        for (var item : universe) {
            map.putIfAbsent(item, value);
        }
    }
}
