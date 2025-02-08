package com.github.milomarten.flagguesser.exception;

import lombok.Getter;

@Getter
public class UnknownFlagCodeException extends RuntimeException {
    private final String flagCode;

    public UnknownFlagCodeException(String flagCode) {
        super("Unknown flag code: " + flagCode);
        this.flagCode = flagCode;
    }
}
