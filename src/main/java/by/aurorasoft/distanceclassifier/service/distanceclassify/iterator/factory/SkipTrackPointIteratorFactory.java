package by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.factory;

import by.aurorasoft.distanceclassifier.config.property.ClassifyDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.SkipTrackPointIterator;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.pointreplacer.TrackPointReplacer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class SkipTrackPointIteratorFactory {
    private final TrackPointReplacer pointReplacer;
    private final ClassifyDistanceProperty property;

    public SkipTrackPointIterator create(final Track track) {
        final double pointMinGpsRelativeInMeters = property.getPointMinGpsRelative() / 1000;
        return new SkipTrackPointIterator(pointReplacer, track.getPoints(), pointMinGpsRelativeInMeters);
    }
}
