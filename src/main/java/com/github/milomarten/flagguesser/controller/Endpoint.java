package com.github.milomarten.flagguesser.controller;

import com.github.milomarten.flagguesser.model.FlagComparison;
import com.github.milomarten.flagguesser.model.FlagRequest;
import com.github.milomarten.flagguesser.model.FlagResponse;
import com.github.milomarten.flagguesser.model.FlagView;
import com.github.milomarten.flagguesser.service.FlagAnswerService;
import com.github.milomarten.flagguesser.service.FlagCompareService;
import com.github.milomarten.flagguesser.service.FlagLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Endpoint {
    @Autowired
    private FlagCompareService flagCompareService;

    @Autowired
    private FlagAnswerService flagAnswerService;

    @Autowired
    private FlagLoader loader;

    @PostMapping("/vexle/guess")
    @CrossOrigin("*")
    public FlagResponse guess(@RequestBody FlagRequest request) {
        var answer = request.getHardCodedAnswer() == null ?
                flagAnswerService.getCodeForDay(request.getDate()) :
                request.getHardCodedAnswer();
        return flagCompareService.multiCompare(answer, request.getGuesses());
    }

    @GetMapping("/vexle/flags")
    @CrossOrigin("*")
    public List<FlagView> flags() {
        return loader.flags()
                .stream()
                .map(FlagView::new)
                .toList();
    }
}
