package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.factory;

import by.aurorasoft.distanceclassifier.config.property.ClassifyingDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.ConnectingTrackPointIterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ConnectingTrackPointIteratorFactory {
    private final ClassifyingDistanceProperty property;

    public ConnectingTrackPointIterator create(final Track track) {
        return null;
//        return new ThrowingTrackPointIterator(track.getPoints(), property.getPointUnionGpsRelativeThreshold());
    }
}
