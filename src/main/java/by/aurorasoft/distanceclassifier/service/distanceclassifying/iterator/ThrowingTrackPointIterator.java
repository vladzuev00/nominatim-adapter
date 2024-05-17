package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static java.lang.Double.compare;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public final class ThrowingTrackPointIterator implements Iterator<TrackPoint> {
    private final List<TrackPoint> points;
    private final double gpsRelativeThreshold;
    private final PointSequenceCursor cursor = new PointSequenceCursor(0, 1);

    @Override
    public boolean hasNext() {
        return cursor.start < points.size();
    }

    @Override
    public TrackPoint next() {
        checkNext();
        final var point = !isSelectedOnePoint() ? getLastSelectedPointThrowingRemaining() : getFirstSelectedPoint();
        selectNextSequence();
        return point;
    }

    private void checkNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
    }

    private boolean isSelectedOnePoint() {
        return cursor.start == cursor.end - 1;
    }

    private TrackPoint getLastSelectedPointThrowingRemaining() {
        final TrackPoint lastPoint = getLastSelectedPoint();
        final Distance gpsDistance = recalculateGpsDistanceLastSelectedPointThrowingRemaining();
        final Distance odometerDistance = recalculateOdometerDistanceLastSelectedPointThrowingRemaining();
        return new TrackPoint(lastPoint.getCoordinate(), lastPoint.getSpeed(), gpsDistance, odometerDistance);
    }

    private Distance recalculateGpsDistanceLastSelectedPointThrowingRemaining() {
        return recalculateDistanceLastSelectedPointThrowingRemaining(TrackPoint::getGpsDistance);
    }

    private Distance recalculateOdometerDistanceLastSelectedPointThrowingRemaining() {
        return recalculateDistanceLastSelectedPointThrowingRemaining(TrackPoint::getOdometerDistance);
    }

    private Distance recalculateDistanceLastSelectedPointThrowingRemaining(final Function<TrackPoint, Distance> getter) {
        final TrackPoint firstPoint = getFirstSelectedPoint();
        final TrackPoint lastPoint = getLastSelectedPoint();
        final Distance firstPointDistance = getter.apply(firstPoint);
        final Distance lastPointDistance = getter.apply(lastPoint);
        final double relative = lastPointDistance.getAbsolute() - firstPointDistance.getAbsolute() - firstPointDistance.getRelative();
        return new Distance(relative, lastPointDistance.getAbsolute());
    }

    private TrackPoint getFirstSelectedPoint() {
        return points.get(cursor.start);
    }

    private TrackPoint getLastSelectedPoint() {
        return points.get(cursor.end - 1);
    }

    private void selectNextSequence() {
        if (!isNoMorePoints()) {
            pickOutNextPoints();
        } else {
            shiftCursorToEnd();
        }
    }

    private boolean isNoMorePoints() {
        return cursor.end == points.size();
    }

    private void pickOutNextPoints() {
        cursor.start = cursor.end;
        cursor.end = findNextCursorNextLastIndex();
    }

    private int findNextCursorNextLastIndex() {
        final int cursorLastIndex = cursor.end - 1;
        return range(cursorLastIndex, points.size())
                .filter(i -> isGpsThresholdExceeded(cursorLastIndex, i))
                .findFirst()
                .orElse(points.size());
    }

    private boolean isGpsThresholdExceeded(final int firstPointIndex, final int secondPointIndex) {
        final double gpsDistance = getPointGpsAbsolute(secondPointIndex) - getPointGpsAbsolute(firstPointIndex);
        return compare(gpsDistance, gpsRelativeThreshold) >= 0;
    }

    private double getPointGpsAbsolute(final int index) {
        return points.get(index).getGpsDistance().getAbsolute();
    }

    private void shiftCursorToEnd() {
        cursor.start = points.size();
        cursor.end = points.size();
    }

    @AllArgsConstructor
    private static class PointSequenceCursor {
        private int start;
        private int end;
    }
}
