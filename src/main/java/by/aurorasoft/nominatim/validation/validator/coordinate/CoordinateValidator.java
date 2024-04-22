package by.aurorasoft.nominatim.validation.validator.coordinate;

import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

import static java.lang.Double.compare;

@RequiredArgsConstructor
public abstract class CoordinateValidator<A extends Annotation> implements ConstraintValidator<A, Double> {
    private final double minAllowable;
    private final double maxAllowable;

    @Override
    public final boolean isValid(final Double value, final ConstraintValidatorContext context) {
        return compare(value, minAllowable) >= 0 && compare(value, maxAllowable) <= 0;
    }
}
