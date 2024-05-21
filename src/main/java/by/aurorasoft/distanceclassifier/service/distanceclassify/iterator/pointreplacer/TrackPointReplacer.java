package by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.pointreplacer;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
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
        final double newReplacementRelative = getAbsoluteDifference(existingDistance, replacementDistance)
                + existingDistance.getRelative();
        return new Distance(newReplacementRelative, replacementDistance.getAbsolute());
    }

    private double getAbsoluteDifference(final Distance existing, final Distance replacement) {
        final double value = replacement.getAbsolute() - existing.getAbsolute();
        if (compare(value, 0) < 0) {
            throw new TrackPointReplacingException("Wrong order: \n\t%s\n\t%s".formatted(existing, replacement));
        }
        return value;
    }

    static final class TrackPointReplacingException extends RuntimeException {

        @SuppressWarnings("unused")
        public TrackPointReplacingException() {

        }

        public TrackPointReplacingException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public TrackPointReplacingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public TrackPointReplacingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
