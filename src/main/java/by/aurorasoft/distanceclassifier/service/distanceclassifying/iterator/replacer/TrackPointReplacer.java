package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.replacer;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public final class TrackPointReplacer {

    public TrackPoint replace(final TrackPoint existing, final TrackPoint replacement) {
        final Distance newGpsDistance = recalculateGpsDistance(existing, replacement);
        final Distance newOdometerDistance = recalculateOdometerDistance(existing, replacement);
        return new TrackPoint(replacement.getCoordinate(), replacement.getSpeed(), newGpsDistance, newOdometerDistance);
    }

    private Distance recalculateGpsDistance(final TrackPoint first, final TrackPoint second) {
        return recalculateDistance(first, second, TrackPoint::getGpsDistance);
    }

    private Distance recalculateOdometerDistance(final TrackPoint first, final TrackPoint second) {
        return recalculateDistance(first, second, TrackPoint::getOdometerDistance);
    }

    private Distance recalculateDistance(final TrackPoint existing,
                                         final TrackPoint replacement,
                                         final Function<TrackPoint, Distance> getter) {
        final Distance existingDistance = getter.apply(existing);
        final Distance replacementDistance = getter.apply(replacement);
        final double newRelative = replacementDistance.getAbsolute()
                - existingDistance.getAbsolute()
                + existingDistance.getRelative();
        return new Distance(newRelative, replacementDistance.getAbsolute());
    }
}
