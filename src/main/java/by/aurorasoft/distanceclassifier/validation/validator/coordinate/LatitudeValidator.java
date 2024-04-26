package by.aurorasoft.distanceclassifier.validation.validator.coordinate;

import by.aurorasoft.distanceclassifier.validation.annotation.Latitude;

public final class LatitudeValidator extends CoordinateValidator<Latitude> {
    private static final double MIN_ALLOWABLE = -90;
    private static final double MAX_ALLOWABLE = 90;

    public LatitudeValidator() {
        super(MIN_ALLOWABLE, MAX_ALLOWABLE);
    }
}
