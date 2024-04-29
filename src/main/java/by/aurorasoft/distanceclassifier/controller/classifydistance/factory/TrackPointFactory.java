package by.aurorasoft.distanceclassifier.controller.classifydistance.factory;

import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest.DistanceRequest;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest.PointRequest;
import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public final class TrackPointFactory {

    public TrackPoint create(final PointRequest request) {
        return new TrackPoint(
                getCoordinate(request),
                request.getSpeed(),
                getGpsDistance(request),
                getOdometerDistance(request)
        );
    }

    private static Coordinate getCoordinate(final PointRequest request) {
        return new Coordinate(request.getLatitude(), request.getLongitude());
    }

    private static Distance getGpsDistance(final PointRequest request) {
        return getDistance(request, PointRequest::getGpsDistance);
    }

    private static Distance getOdometerDistance(final PointRequest request) {
        return getDistance(request, PointRequest::getOdometerDistance);
    }

    private static Distance getDistance(final PointRequest request,
                                        final Function<PointRequest, DistanceRequest> sourceGetter) {
        final DistanceRequest source = sourceGetter.apply(request);
        return new Distance(source.getRelative(), source.getAbsolute());
    }
}