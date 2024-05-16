package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;

import by.aurorasoft.distanceclassifier.config.property.ClassifyingDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UnionTrackPointIteratorFactory {
    private final ClassifyingDistanceProperty property;

    public UnionTrackPointIterator create(final Track track) {
        return new UnionTrackPointIterator(track.getPoints(), property.getPointUnionGpsRelativeThreshold());
    }
}
