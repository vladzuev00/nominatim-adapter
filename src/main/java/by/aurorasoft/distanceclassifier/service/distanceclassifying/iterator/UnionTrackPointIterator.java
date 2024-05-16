package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static java.lang.Double.compare;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public final class UnionTrackPointIterator implements Iterator<TrackPoint> {
    private final List<TrackPoint> points;
    private final double gpsRelativeThreshold;
    private final Cursor cursor = new Cursor();

    @Override
    public boolean hasNext() {
        return cursor.firstIndex < points.size();
    }

    @Override
    public TrackPoint next() {
        final TrackPoint point = getNextUnion();
        shiftCursor();
        return point;
    }

    private TrackPoint getNextUnion() {
        return !isNextUnionContainOnePoint() ? getNextUnionLastPointRecalculatingRelative() : getNextUnionLastPoint();
    }

    private boolean isNextUnionContainOnePoint() {
        return cursor.firstIndex == cursor.nextLastIndex - 1;
    }

    private TrackPoint getNextUnionLastPointRecalculatingRelative() {
        final TrackPoint first = getNextUnionFirstPoint();
        final TrackPoint last = getNextUnionLastPoint();
        final Distance gpsDistance = findGpsDistanceRecalculatingRelative(first, last);
        final Distance odometerDistance = findOdometerDistanceRecalculatingRelative(first, last);
        return new TrackPoint(last.getCoordinate(), last.getSpeed(), gpsDistance, odometerDistance);
    }

    private Distance findGpsDistanceRecalculatingRelative(final TrackPoint first, final TrackPoint second) {
        return findDistanceRecalculatingRelative(first, second, TrackPoint::getGpsDistance);
    }

    private Distance findOdometerDistanceRecalculatingRelative(final TrackPoint first, final TrackPoint second) {
        return findDistanceRecalculatingRelative(first, second, TrackPoint::getOdometerDistance);
    }

    private Distance findDistanceRecalculatingRelative(final TrackPoint first,
                                                       final TrackPoint second,
                                                       final Function<TrackPoint, Distance> distanceGetter) {
        final Distance firstDistance = distanceGetter.apply(first);
        final Distance secondDistance = distanceGetter.apply(second);
        final double relative = secondDistance.getAbsolute() - firstDistance.getAbsolute();
        return new Distance(relative, secondDistance.getAbsolute());
    }

    private TrackPoint getNextUnionFirstPoint() {
        return points.get(cursor.firstIndex);
    }

    private TrackPoint getNextUnionLastPoint() {
        return points.get(cursor.nextLastIndex - 1);
    }

    private void shiftCursor() {
        if (!isNextUnionLast()) {
            shiftCursorToNextUnion();
        } else {
            shiftCursorToEnd();
        }
    }

    private boolean isNextUnionLast() {
        return cursor.nextLastIndex == points.size();
    }

    private void shiftCursorToNextUnion() {
        final int nextLastNextUnionIndex = range(cursor.nextLastIndex - 1, points.size())
                .filter(i -> isGpsThresholdExceeded(cursor.nextLastIndex - 1, i))
                .findFirst()
                .orElse(points.size());
        cursor.firstIndex = cursor.nextLastIndex - 1;
        cursor.nextLastIndex = nextLastNextUnionIndex;
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

    private static final class Cursor {
        private int firstIndex;
        private int nextLastIndex;
    }
}
