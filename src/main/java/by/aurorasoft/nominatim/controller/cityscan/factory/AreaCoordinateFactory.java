package by.aurorasoft.nominatim.controller.cityscan.factory;

import by.aurorasoft.nominatim.controller.cityscan.model.AreaCoordinateRequest;
import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.model.Coordinate;
import org.springframework.stereotype.Component;

@Component
public final class AreaCoordinateFactory {

    public AreaCoordinate create(final AreaCoordinateRequest request) {
        return new AreaCoordinate(getMinCoordinate(request), getMaxCoordinate(request));
    }

    private Coordinate getMinCoordinate(final AreaCoordinateRequest request) {
        return new Coordinate(request.getMinLatitude(), request.getMinLongitude());
    }

    private Coordinate getMaxCoordinate(final AreaCoordinateRequest request) {
        return new Coordinate(request.getMaxLongitude(), request.getMinLongitude());
    }
}
