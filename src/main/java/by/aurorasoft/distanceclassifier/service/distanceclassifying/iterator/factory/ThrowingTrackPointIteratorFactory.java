package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.factory;

import by.aurorasoft.distanceclassifier.config.property.ClassifyingDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.ConnectingTrackPointIterator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ThrowingTrackPointIteratorFactory {
    private final ClassifyingDistanceProperty property;

    public ConnectingTrackPointIterator create(final Track track) {
        return null;
//        return new ThrowingTrackPointIterator(track.getPoints(), property.getPointUnionGpsRelativeThreshold());
    }
}
