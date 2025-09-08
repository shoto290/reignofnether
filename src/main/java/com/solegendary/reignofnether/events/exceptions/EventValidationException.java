package com.solegendary.reignofnether.events.exceptions;

import com.solegendary.reignofnether.events.validation.ValidationResult;

public class EventValidationException extends Exception {
    private final ValidationResult validationResult;

    public EventValidationException(ValidationResult validationResult) {
        super(validationResult.getErrorMessage());
        this.validationResult = validationResult;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}