package by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.pointreplacer;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.exception.TrackPointWrongOrderException;
import by.nhorushko.classifieddistance.Distance;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.lang.Double.compare;

@Component
public final class TrackPointReplacer {

    public TrackPoint replace(final TrackPoint existing, final TrackPoint replacement) {
        final Distance newGpsDistance = recalculateGpsDistance(existing, replacement);
        final Distance newOdometerDistance = recalculateOdometerDistance(existing, replacement);
        return new TrackPoint(replacement.getCoordinate(), replacement.getSpeed(), newGpsDistance, newOdometerDistance);
    }

    private Distance recalculateGpsDistance(final TrackPoint existing, final TrackPoint replacement) {
        return recalculateDistance(existing, replacement, TrackPoint::getGpsDistance);
    }

    private Distance recalculateOdometerDistance(final TrackPoint existing, final TrackPoint replacement) {
        return recalculateDistance(existing, replacement, TrackPoint::getOdometerDistance);
    }

    private Distance recalculateDistance(final TrackPoint existing,
                                         final TrackPoint replacement,
                                         final Function<TrackPoint, Distance> getter) {
        final Distance existingDistance = getter.apply(existing);
        final Distance replacementDistance = getter.apply(replacement);
        final double absoluteDifference = replacementDistance.getAbsolute() - existingDistance.getAbsolute();
        if (compare(absoluteDifference, 0) < 0) {
            throwWrongOrderException(existing, replacement);
        }
        final double newReplacementRelative = absoluteDifference + existingDistance.getRelative();
        return new Distance(newReplacementRelative, replacementDistance.getAbsolute());
    }

    private void throwWrongOrderException(final TrackPoint existing, final TrackPoint replacement)
            throws TrackPointWrongOrderException {
        throw new TrackPointWrongOrderException(
                """
                        Absolute difference was negative
                        Existing point: %s
                        Replacement: %s"""
                        .formatted(existing, replacement)
        );
    }
}
