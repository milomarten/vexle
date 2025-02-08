package com.github.milomarten.flagguesser.service;

import com.github.milomarten.flagguesser.model.Flag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FlagAnswerService {
    private final FlagLoader loader;

    private List<String> codes;

    @PostConstruct
    private void setUp() {
        this.codes = this.loader.flags()
                .stream()
                .sorted(Comparator.comparing(Flag::getCountryCode))
                .map(Flag::getCountryCode)
                .toList();
    }

    public String getCodeForDay(LocalDate ld) {
        var year = ld.getYear() - 2000;
        var month = ld.getMonthValue();
        var day = ld.getDayOfMonth();

        var idx = Objects.hash(year, month, day) % codes.size();
        return codes.get(idx);
    }
}
