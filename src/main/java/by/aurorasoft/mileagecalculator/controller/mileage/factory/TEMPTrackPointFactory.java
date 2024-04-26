package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.TEMPMileageRequest.TEMPTrackPointRequest;
import by.aurorasoft.mileagecalculator.model.Coordinate;
import by.aurorasoft.mileagecalculator.model.TrackPoint;
import org.springframework.stereotype.Component;

@Component
public final class TEMPTrackPointFactory {

    public TrackPoint create(final TEMPTrackPointRequest request) {
//        return new TrackPoint(
//                request.getDatetime(),
//                getCoordinate(request),
//                request.getAltitude(),
//                request.getSpeed(),
//                request.getValid()
//        );
        return null;
    }

    private Coordinate getCoordinate(final TEMPTrackPointRequest request) {
        return new Coordinate(request.getLatitude(), request.getLongitude());
    }
}
