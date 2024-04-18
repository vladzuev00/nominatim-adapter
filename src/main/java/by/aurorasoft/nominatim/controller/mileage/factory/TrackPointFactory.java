package by.aurorasoft.nominatim.controller.mileage.factory;

import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest.TrackPointRequest;
import by.aurorasoft.nominatim.model.Coordinate;
import by.aurorasoft.nominatim.model.TrackPoint;
import org.springframework.stereotype.Component;

@Component
public final class TrackPointFactory {

    public TrackPoint create(final TrackPointRequest request) {
        return new TrackPoint(
                request.getDatetime(),
                getCoordinate(request),
                request.getAltitude(),
                request.getSpeed(),
                request.getValid()
        );
    }

    private Coordinate getCoordinate(final TrackPointRequest request) {
        return new Coordinate(request.getLatitude(), request.getLongitude());
    }
}
