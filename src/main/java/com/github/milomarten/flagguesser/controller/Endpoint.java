package com.github.milomarten.flagguesser.controller;

import com.github.milomarten.flagguesser.model.FlagComparison;
import com.github.milomarten.flagguesser.model.FlagRequest;
import com.github.milomarten.flagguesser.service.FlagAnswerService;
import com.github.milomarten.flagguesser.service.FlagCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Endpoint {
    @Autowired
    private FlagCompareService flagCompareService;

    @Autowired
    private FlagAnswerService flagAnswerService;

    @PostMapping("/vexle/guess")
    public FlagComparison guess(FlagRequest request) {
        var answer = request.getHardCodedAnswer() == null ?
                flagAnswerService.getCodeForDay(request.getDate()) :
                request.getHardCodedAnswer();
        return flagCompareService.multiCompare(answer, request.getGuesses());
    }
}
