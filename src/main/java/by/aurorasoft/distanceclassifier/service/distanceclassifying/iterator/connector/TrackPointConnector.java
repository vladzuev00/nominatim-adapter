package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.connector;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public final class TrackPointConnector {

    public TrackPoint connect(final TrackPoint first, final TrackPoint second) {
        return !isSamePoint(first, second) ? connectDifferentPoints(first, second) : second;
    }

    private boolean isSamePoint(final TrackPoint first, final TrackPoint second) {
        return first == second || Objects.equals(first.getCoordinate(), second.getCoordinate());
    }

    private TrackPoint connectDifferentPoints(final TrackPoint first, final TrackPoint second) {
        final Distance newGpsDistance = recalculateGpsDistance(first, second);
        final Distance newOdometerDistance = recalculateOdometerDistance(first, second);
        return new TrackPoint(second.getCoordinate(), second.getSpeed(), newGpsDistance, newOdometerDistance);
    }

    private Distance recalculateGpsDistance(final TrackPoint first, final TrackPoint second) {
        return recalculateDistance(first, second, TrackPoint::getGpsDistance);
    }

    private Distance recalculateOdometerDistance(final TrackPoint first, final TrackPoint second) {
        return recalculateDistance(first, second, TrackPoint::getOdometerDistance);
    }

    private Distance recalculateDistance(final TrackPoint first,
                                         final TrackPoint second,
                                         final Function<TrackPoint, Distance> getter) {
        final Distance firstDistance = getter.apply(first);
        final Distance secondDistance = getter.apply(second);
        final double newRelative = secondDistance.getAbsolute() - firstDistance.getAbsolute();
        return new Distance(newRelative, secondDistance.getAbsolute());
    }
}
