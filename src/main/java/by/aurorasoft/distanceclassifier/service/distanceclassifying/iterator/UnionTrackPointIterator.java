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
public final class UnionTrackPointIterator implements Iterator<TrackPoint> {
    private final List<TrackPoint> points;
    private final double gpsRelativeThreshold;
    private final Cursor cursor = new Cursor(0, 1);

    @Override
    public boolean hasNext() {
        return cursor.firstIndex < points.size();
    }

    @Override
    public TrackPoint next() {
        checkNext();
        final TrackPoint point = unionCursorPoints();
        shiftCursor();
        return point;
    }

    private void checkNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
    }

    private TrackPoint unionCursorPoints() {
        return !isCursorPointAtOnePoint() ? getCursorLastPointRecalculatingRelative() : getCursorLastPoint();
    }

    private boolean isCursorPointAtOnePoint() {
        return cursor.firstIndex == cursor.nextLastIndex - 1;
    }

    private TrackPoint getCursorLastPointRecalculatingRelative() {
        final TrackPoint lastPoint = getCursorLastPoint();
        final Distance gpsDistance = findCursorLastPointGpsDistanceSkippingCursorInnerPoints();
        final Distance odometerDistance = findCursorLastPointOdometerDistanceSkippingCursorInnerPoints();
        return new TrackPoint(lastPoint.getCoordinate(), lastPoint.getSpeed(), gpsDistance, odometerDistance);
    }

    private Distance findCursorLastPointGpsDistanceSkippingCursorInnerPoints() {
        return findCursorLastPointDistanceSkippingCursorInnerPoints(TrackPoint::getGpsDistance);
    }

    private Distance findCursorLastPointOdometerDistanceSkippingCursorInnerPoints() {
        return findCursorLastPointDistanceSkippingCursorInnerPoints(TrackPoint::getOdometerDistance);
    }

    private Distance findCursorLastPointDistanceSkippingCursorInnerPoints(final Function<TrackPoint, Distance> distanceGetter) {
        final TrackPoint firstPoint = getCursorFirstPoint();
        final TrackPoint lastPoint = getCursorLastPoint();
        final Distance firstPointDistance = distanceGetter.apply(firstPoint);
        final Distance lastPointDistance = distanceGetter.apply(lastPoint);
        final double relative = lastPointDistance.getAbsolute() - firstPointDistance.getAbsolute();
        return new Distance(relative, lastPointDistance.getAbsolute());
    }

    private TrackPoint getCursorFirstPoint() {
        return points.get(cursor.firstIndex);
    }

    private TrackPoint getCursorLastPoint() {
        return points.get(cursor.nextLastIndex - 1);
    }

    private void shiftCursor() {
        if (!isNoMorePoints()) {
            pickOutNextPoints();
        } else {
            shiftCursorToEnd();
        }
    }

    private boolean isNoMorePoints() {
        return cursor.nextLastIndex == points.size();
    }

    private void pickOutNextPoints() {
        final int newCursorNextLastIndex = range(cursor.nextLastIndex - 1, points.size())
                .filter(i -> isGpsThresholdExceeded(cursor.nextLastIndex - 1, i))
                .findFirst()
                .orElse(0);

//                .stream()
//                .mapToObj(i -> new Cursor(cursor.nextLastIndex - 1, i))
//                .findFirst()
//                .orElseGet(() -> new Cursor(cursor.nextLastIndex - 1, points.size()));
    }

    private boolean isGpsThresholdExceeded(final int firstPointIndex, final int secondPointIndex) {
        final double gpsDistance = getPointGpsAbsolute(secondPointIndex) - getPointGpsAbsolute(firstPointIndex);
        return compare(gpsDistance, gpsRelativeThreshold) >= 0;
    }

    private double getPointGpsAbsolute(final int index) {
        return points.get(index).getGpsDistance().getAbsolute();
    }

    private void shiftCursorToEnd() {
        cursor.firstIndex = points.size();
        cursor.nextLastIndex = points.size();
    }

    @AllArgsConstructor
    private static class Cursor {
        private int firstIndex;
        private int nextLastIndex;
    }
}
