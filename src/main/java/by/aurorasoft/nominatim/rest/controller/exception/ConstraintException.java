package by.aurorasoft.nominatim.rest.controller.exception;

import org.springframework.validation.Errors;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public final class ConstraintException extends RuntimeException {
    private final Errors errors;

    public ConstraintException(Errors errors) {
        this.errors = errors;
    }

    public ConstraintException(String description) {
        super(description);
        this.errors = null;
    }

    public ConstraintException(Exception cause) {
        super(cause);
        this.errors = null;
    }

    public ConstraintException(String description, Exception cause) {
        super(description, cause);
        this.errors = null;
    }

    public Optional<Errors> findErrors() {
        return ofNullable(this.errors);
    }
}
