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
public final class ConnectingTrackPointIterator implements Iterator<TrackPoint> {
    private final TrackPointConnector pointConnector;
    private final List<TrackPoint> points;
    private final double pointMinGpsRelative;
    private final PointSequenceCursor cursor = new PointSequenceCursor();

    @Override
    public boolean hasNext() {
        return cursor.start < points.size();
    }

    @Override
    public TrackPoint next() {
        checkNext();
        final TrackPoint point = connectBoundarySequencePoints();
        trySelectNextSequence();
        return point;
    }

    private void checkNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
    }

    private TrackPoint connectBoundarySequencePoints() {
        final TrackPoint first = points.get(cursor.start);
        final TrackPoint last = points.get(cursor.end);
        return pointConnector.connect(first, last);
    }

    private void trySelectNextSequence() {
        if (!isNoMoreSequences()) {
            selectNextSequence();
        } else {
            shiftCursorToEnd();
        }
    }

    private boolean isNoMoreSequences() {
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
        cursor.end = range(cursor.start, points.size())
                .filter(i -> isSuitableGpsRelativeToBeSequence(cursor.start, i))
                .findFirst()
                .orElse(points.size() - 1);
    }

    private boolean isSuitableGpsRelativeToBeSequence(final int fromIndex, final int toIndex) {
        final double gpsRelative = getPointGpsAbsolute(toIndex) - getPointGpsAbsolute(fromIndex);
        return compare(gpsRelative, pointMinGpsRelative) >= 0;
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
