package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;

import by.aurorasoft.distanceclassifier.config.property.ClassifyingDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ThrowingTrackPointIteratorFactory {
    private final ClassifyingDistanceProperty property;

    public ThrowingTrackPointIterator create(final Track track) {
        return null;
//        return new ThrowingTrackPointIterator(track.getPoints(), property.getPointUnionGpsRelativeThreshold());
    }
}
