package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.replacer.TrackPointConnector;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Double.compare;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public final class ThrowingTrackPointIterator implements Iterator<TrackPoint> {
    private final TrackPointConnector pointConnector;
    private final List<TrackPoint> points;
    private final double gpsRelativeThreshold;
    private final PointSequenceCursor cursor = new PointSequenceCursor();

    @Override
    public boolean hasNext() {
        return cursor.start < points.size();
    }

    @Override
    public TrackPoint next() {
        checkNext();
        final TrackPoint point = replaceSequenceByLastPoint();
        trySelectNextSequence();
        return point;
    }

    private void checkNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
    }

    private TrackPoint replaceSequenceByLastPoint() {
        final TrackPoint first = getFirstSequencePoint();
        final TrackPoint last = getLastSequencePoint();
        return pointConnector.connect(first, last);
    }

    private TrackPoint getFirstSequencePoint() {
        return points.get(cursor.start);
    }

    private TrackPoint getLastSequencePoint() {
        return points.get(cursor.end);
    }

    private void trySelectNextSequence() {
        if (!isNoMorePoints()) {
            selectNextSequence();
        } else {
            shiftCursorToEnd();
        }
    }

    private boolean isNoMorePoints() {
        return cursor.end == points.size() - 1;
    }

    private void selectNextSequence() {
        selectStartNextSequence();
        selectEndNextSequence();
    }

    private void selectStartNextSequence() {
        cursor.start = cursor.end;
    }

    private void selectEndNextSequence() {
        cursor.end = range(cursor.end, points.size())
                .filter(i -> isGpsThresholdExceeded(cursor.end, i))
                .findFirst()
                .orElse(points.size() - 1);
    }

    private boolean isGpsThresholdExceeded(final int firstPointIndex, final int secondPointIndex) {
        final double gpsDistance = getPointGpsAbsolute(secondPointIndex) - getPointGpsAbsolute(firstPointIndex);
        return compare(gpsDistance, gpsRelativeThreshold) >= 0;
    }

    private double getPointGpsAbsolute(final int pointIndex) {
        return points.get(pointIndex)
                .getGpsDistance()
                .getAbsolute();
    }

    private void shiftCursorToEnd() {
        cursor.start = points.size();
        cursor.end = points.size();
    }

    private static class PointSequenceCursor {
        private int start;
        private int end;
    }
}
