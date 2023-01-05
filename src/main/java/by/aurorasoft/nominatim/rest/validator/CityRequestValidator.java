package by.aurorasoft.nominatim.rest.validator;

import by.aurorasoft.nominatim.rest.controller.exception.ConstraintException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public final class CityRequestValidator {

    public void validate(Errors errors) {
        if (errors.hasErrors()) {
            throw new ConstraintException(errors);
        }
    }
}
