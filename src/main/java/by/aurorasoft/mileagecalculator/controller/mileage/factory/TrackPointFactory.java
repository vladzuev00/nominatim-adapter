package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.TempMileageRequest.TempTrackPointRequest;
import by.aurorasoft.mileagecalculator.model.Coordinate;
import by.aurorasoft.mileagecalculator.model.TrackPoint;
import org.springframework.stereotype.Component;

@Component
public final class TrackPointFactory {

    public TrackPoint create(final TempTrackPointRequest request) {
        return new TrackPoint(
                request.getDatetime(),
                getCoordinate(request),
                request.getAltitude(),
                request.getSpeed(),
                request.getValid()
        );
    }

    private Coordinate getCoordinate(final TempTrackPointRequest request) {
        return new Coordinate(request.getLatitude(), request.getLongitude());
    }
}
