package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;

import static java.lang.Double.compare;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public final class SignificantTrackPointIterator implements Iterator<TrackPoint> {
    private final List<TrackPoint> points;
    private final double gpsThreshold;
    private int nextIndex;

    @Override
    public boolean hasNext() {
        return nextIndex != points.size();
    }

    @Override
    public TrackPoint next() {
        final TrackPoint point = points.get(nextIndex);
        shiftNextIndex();
        return point;
    }

    private void shiftNextIndex() {
        if (!isNextLast()) {
            putNextIndexBeforeNextPoint();
        } else {
            putNextIndexToTheEnd();
        }
    }

    private boolean isNextLast() {
        return nextIndex == points.size() - 1;
    }

    private void putNextIndexBeforeNextPoint() {
        nextIndex = range(nextIndex + 1, points.size())
                .filter(this::isGpsThresholdExceeded)
                .findFirst()
                .orElse(points.size() - 1);
    }

    private void putNextIndexToTheEnd() {
        nextIndex = points.size();
    }

    private boolean isGpsThresholdExceeded(final int index) {
        final double gpsDistance = getPointGpsAbsolute(index) - getPointGpsAbsolute(nextIndex);
        return compare(gpsDistance, gpsThreshold) >= 0;
    }

    private double getPointGpsAbsolute(final int index) {
        return points.get(index).getGpsDistance().getAbsolute();
    }


}
