package by.aurorasoft.nominatim.controller.cityscan.factory;

import by.aurorasoft.nominatim.controller.cityscan.model.AreaCoordinateRequest;
import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.model.Coordinate;
import by.aurorasoft.nominatim.controller.exception.CustomValidationException;
import org.springframework.stereotype.Component;

import static java.lang.Double.compare;

@Component
public final class AreaCoordinateFactory {

    public AreaCoordinate create(final AreaCoordinateRequest request) {
        validate(request);
        return new AreaCoordinate(getMinCoordinate(request), getMaxCoordinate(request));
    }

    private static void validate(final AreaCoordinateRequest request) {
        if (!isValidAreaCoordinate(request)) {
            throw new CustomValidationException("Min coordinate of area should be less than max");
        }
    }

    private static boolean isValidAreaCoordinate(final AreaCoordinateRequest request) {
        return compare(request.getMinLatitude(), request.getMaxLatitude()) <= 0
                && compare(request.getMinLongitude(), request.getMaxLongitude()) <= 0;
    }

    private Coordinate getMinCoordinate(final AreaCoordinateRequest request) {
        return new Coordinate(request.getMinLatitude(), request.getMinLongitude());
    }

    private Coordinate getMaxCoordinate(final AreaCoordinateRequest request) {
        return new Coordinate(request.getMaxLatitude(), request.getMaxLongitude());
    }
}
