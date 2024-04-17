package by.aurorasoft.nominatim.controller.mileage.factory;

import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest.TrackPointRequest;
import by.aurorasoft.nominatim.model.TrackPoint;
import org.springframework.stereotype.Component;

@Component
public final class TrackPointFactory {

    public TrackPoint create(final TrackPointRequest request) {
        return new TrackPoint(
                request.getDatetime(),
                request.getLatitude(),
                request.getLongitude(),
                request.getAltitude(),
                request.getSpeed(),
                request.getValid()
        );
    }
}
