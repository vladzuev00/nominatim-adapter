package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.factory;

import by.aurorasoft.distanceclassifier.config.property.ClassifyDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.SkippingTrackPointIterator;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.pointreplacer.TrackPointReplacer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class SkippingTrackPointIteratorFactory {
    private final TrackPointReplacer pointReplacer;
    private final ClassifyDistanceProperty property;

    public SkippingTrackPointIterator create(final Track track) {
        return new SkippingTrackPointIterator(pointReplacer, track.getPoints(), property.getPointMinGpsRelative());
    }
}
