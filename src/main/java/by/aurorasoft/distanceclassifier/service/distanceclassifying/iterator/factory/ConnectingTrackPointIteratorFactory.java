package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.factory;

import by.aurorasoft.distanceclassifier.config.property.ClassifyingDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.ConnectingTrackPointIterator;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.pointreplacer.TrackPointReplacer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ConnectingTrackPointIteratorFactory {
    private final TrackPointReplacer pointConnector;
    private final ClassifyingDistanceProperty property;

    public ConnectingTrackPointIterator create(final Track track) {
        return new ConnectingTrackPointIterator(pointConnector, track.getPoints(), property.getPointMinGpsRelative());
    }
}
