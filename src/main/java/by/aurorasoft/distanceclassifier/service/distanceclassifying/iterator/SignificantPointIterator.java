package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;

import static java.lang.Double.compare;
import static java.util.stream.IntStream.rangeClosed;

@RequiredArgsConstructor
public final class SignificantPointIterator implements Iterator<TrackPoint> {
    private final List<TrackPoint> trackPoints;
    private final double gpsDistanceThreshold;
    private int nextIndex;

    @Override
    public boolean hasNext() {
        return nextIndex < trackPoints.size();
    }

    @Override
    public TrackPoint next() {
        final TrackPoint point = trackPoints.get(nextIndex);
        nextIndex = findShiftedNextIndex();
        return point;
    }

    private int findShiftedNextIndex() {
        final int lastIndex = trackPoints.size() - 1;
        return rangeClosed(nextIndex + 1, lastIndex)
                .filter(this::isThresholdExceeded)
                .findFirst()
                .orElse(lastIndex);
    }

    private boolean isThresholdExceeded(final int pointIndex) {
        final double distance = getPointGpsAbsolute(pointIndex) - getPointGpsAbsolute(nextIndex);
        return compare(distance, gpsDistanceThreshold) >= 0;
    }

    private double getPointGpsAbsolute(final int index) {
        return trackPoints.get(index).getGpsDistance().getAbsolute();
    }
}
