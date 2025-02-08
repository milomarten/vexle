package com.github.milomarten.flagguesser.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FlagRequest {
    private String hardCodedAnswer; // To be used during test trials. Eventually there will be one a day.
    private LocalDate date;
    private String[] guesses;
}
