package by.aurorasoft.nominatim.validation.validator.coordinate;

import by.aurorasoft.nominatim.validation.annotation.Latitude;

public final class LatitudeValidator extends CoordinateValidator<Latitude> {
    private static final double MIN_ALLOWABLE = -90;
    private static final double MAX_ALLOWABLE = 90;

    public LatitudeValidator() {
        super(MIN_ALLOWABLE, MAX_ALLOWABLE);
    }
}
