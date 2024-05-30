package by.aurorasoft.distanceclassifier.service.distanceclassify.iterator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.exception.TrackPointWrongOrderException;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.pointreplacer.TrackPointReplacer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Double.compare;
import static java.util.stream.IntStream.range;

@Slf4j
@RequiredArgsConstructor
public final class SkipTrackPointIterator implements Iterator<TrackPoint> {
    private final TrackPointReplacer pointReplacer;
    private final List<TrackPoint> points;
    private final double pointMinGpsRelative;
    private final SequenceCursor cursor = new SequenceCursor(0, 1);

    @Override
    public boolean hasNext() {
        return cursor.start < points.size();
    }

    @Override
    public TrackPoint next() {
        checkNext();
        final TrackPoint sequenceLastPoint = replaceSequenceByLastPoint();
        trySelectNextSequence();
        return sequenceLastPoint;
    }

    private void checkNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
    }

    private TrackPoint replaceSequenceByLastPoint() {
        try {
            final TrackPoint first = points.get(cursor.start);
            final TrackPoint last = points.get(cursor.end - 1);
            return !isSequenceContainOnePoint() ? pointReplacer.replace(first, last) : last;
        } catch (final TrackPointWrongOrderException exception) {
            logPointWrongOrderInSequence();
            throw exception;
        }
    }

    private boolean isSequenceContainOnePoint() {
        return cursor.start == cursor.end - 1;
    }

    private void trySelectNextSequence() {
        if (hasNextSequence()) {
            selectNextSequence();
        } else {
            shiftCursorToEnd();
        }
    }

    private boolean hasNextSequence() {
        return cursor.end != points.size();
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
                .filter(i -> isSuitableGpsRelativeToBeSequence(cursor.end, i))
                .findFirst()
                .orElse(points.size() - 1) + 1;
    }

    private boolean isSuitableGpsRelativeToBeSequence(final int fromIndex, final int toIndex) {
        final double gpsRelative = getPointGpsAbsolute(toIndex) - getPointGpsAbsolute(fromIndex - 1);
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

    private void logPointWrongOrderInSequence() {
        log.error("Sequence contain point with wrong order: {}", points.subList(cursor.start, cursor.end));
    }

    @AllArgsConstructor
    private static final class SequenceCursor {
        private int start;
        private int end;
    }
}
