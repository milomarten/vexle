package com.github.milomarten.flagguesser.controller;

import com.github.milomarten.flagguesser.model.*;
import com.github.milomarten.flagguesser.service.FlagAnswerService;
import com.github.milomarten.flagguesser.service.FlagCompareService;
import com.github.milomarten.flagguesser.service.FlagLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Endpoint {
    @Autowired
    private FlagCompareService flagCompareService;

    @Autowired
    private FlagAnswerService flagAnswerService;

    @Autowired
    private FlagLoader loader;

    @PostMapping("/guess")
    @CrossOrigin("*")
    public FlagResponse guess(@RequestBody FlagRequest request) {
        var answer = request.getHardCodedAnswer() == null ?
                flagAnswerService.getCodeForDay(request.getDate()) :
                request.getHardCodedAnswer();
//        return flagCompareService.multiCompare(answer, request.getGuesses());
        return flagCompareService.compare(request.getGuesses(), answer);
    }

    @GetMapping("/flags")
    @CrossOrigin("*")
    public List<FlagView> flags() {
        return loader.flags()
                .stream()
                .map(FlagView::new)
                .toList();
    }
}
