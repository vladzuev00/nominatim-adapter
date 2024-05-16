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
public final class SignificantTrackPointIterator implements Iterator<TrackPoint> {
    private final List<TrackPoint> points;
    private final double gpsThreshold;
    private final UnionIndex nextUnionIndex;

    public SignificantTrackPointIterator(final List<TrackPoint> points, final double gpsThreshold) {
        this.points = points;
        this.gpsThreshold = gpsThreshold;
        nextUnionIndex = new UnionIndex();
    }

    @Override
    public boolean hasNext() {
        return nextUnionIndex.first < points.size();
    }

    @Override
    public TrackPoint next() {
        final TrackPoint point = getNextUnion();
        shiftNextUnionIndex();
        return point;
    }

    private TrackPoint getNextUnion() {
        final TrackPoint first = points.get(nextUnionIndex.first);
        final TrackPoint last = points.get(nextUnionIndex.last);
        if (last == first) {
            return last;
        }
        final double gpsRelative = findGpsRelative(first, last);
        final double odometerRelative = findOdometerRelative(first, last);
        return changeRelative(last, gpsRelative, odometerRelative);
    }

    private double findGpsRelative(final TrackPoint first, final TrackPoint second) {
        return findRelative(first, second, TrackPoint::getGpsDistance);
    }

    private double findOdometerRelative(final TrackPoint first, final TrackPoint second) {
        return findRelative(first, second, TrackPoint::getOdometerDistance);
    }

    private double findRelative(final TrackPoint first,
                                final TrackPoint second,
                                final Function<TrackPoint, Distance> getter) {
        final Distance secondDistance = getter.apply(second);
        return first != second
                ? secondDistance.getAbsolute() - getter.apply(first).getAbsolute()
                : secondDistance.getRelative();
    }

    private TrackPoint changeRelative(final TrackPoint point, final double gps, final double odometer) {
        final Distance gpsDistance = new Distance(gps, point.getGpsDistance().getAbsolute());
        final Distance odometerDistance = new Distance(odometer, point.getOdometerDistance().getAbsolute());
        return new TrackPoint(point.getCoordinate(), point.getSpeed(), gpsDistance, odometerDistance);
    }

    private void shiftNextUnionIndex() {
        if (!isNextLast()) {
            shiftNextUnionIndexToNextUnion();
        } else {
            putNextUnionIndexToTheEnd();
        }
    }

    private boolean isNextLast() {
        return nextUnionIndex.last == points.size() - 1;
    }

    private void shiftNextUnionIndexToNextUnion() {
        final int nextLastNextUnionIndex = range(nextUnionIndex.last, points.size())
                .filter(this::isGpsThresholdExceeded)
                .findFirst()
                .orElse(points.size() - 1);
        nextUnionIndex.first = nextUnionIndex.last;
        nextUnionIndex.last = nextLastNextUnionIndex;
    }

    private boolean isGpsThresholdExceeded(final int index) {
        final double gpsDistance = getPointGpsAbsolute(index) - getPointGpsAbsolute(nextUnionIndex.last);
        return compare(gpsDistance, gpsThreshold) >= 0;
    }

    private double getPointGpsAbsolute(final int index) {
        return points.get(index).getGpsDistance().getAbsolute();
    }

    private void putNextUnionIndexToTheEnd() {
        nextUnionIndex.first = points.size();
        nextUnionIndex.last = points.size();
    }

    private static class UnionIndex {
        private int first;
        private int last;
    }
}
