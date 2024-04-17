package by.aurorasoft.nominatim.validation.validator;

import by.aurorasoft.nominatim.validation.annotation.CityName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public final class CityNameValidator implements ConstraintValidator<CityName, String> {
    private static final String REGEX = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    private static final Pattern PATTERN = compile(REGEX);

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return value != null && PATTERN.matcher(value).matches();
    }
}
