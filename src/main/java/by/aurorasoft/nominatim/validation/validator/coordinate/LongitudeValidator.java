package by.aurorasoft.nominatim.validation.validator.coordinate;

import by.aurorasoft.nominatim.validation.annotation.Longitude;

public final class LongitudeValidator extends CoordinateValidator<Longitude> {
    private static final double MIN_ALLOWABLE = -180;
    private static final double MAX_ALLOWABLE = 180;

    public LongitudeValidator() {
        super(MIN_ALLOWABLE, MAX_ALLOWABLE);
    }
}
