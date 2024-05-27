package by.aurorasoft.distanceclassifier.controller.cityscan.factory;

import by.aurorasoft.distanceclassifier.controller.cityscan.model.AreaCoordinateRequest;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.controller.exception.CustomValidationException;
import org.springframework.stereotype.Component;

import static java.lang.Double.compare;

@Component
public final class AreaCoordinateFactory {

    public AreaCoordinate create(final AreaCoordinateRequest request) {
        validate(request);
        return new AreaCoordinate(getMin(request), getMax(request));
    }

    private void validate(final AreaCoordinateRequest request) {
        if (!isValid(request)) {
            throw new CustomValidationException("Min coordinate of area should be less than max");
        }
    }

    private boolean isValid(final AreaCoordinateRequest request) {
        return compare(request.getMinLatitude(), request.getMaxLatitude()) <= 0
                && compare(request.getMinLongitude(), request.getMaxLongitude()) <= 0;
    }

    private Coordinate getMin(final AreaCoordinateRequest request) {
        return new Coordinate(request.getMinLatitude(), request.getMinLongitude());
    }

    private Coordinate getMax(final AreaCoordinateRequest request) {
        return new Coordinate(request.getMaxLatitude(), request.getMaxLongitude());
    }
}
