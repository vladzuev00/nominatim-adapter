package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.simplifier;

import by.aurorasoft.distanceclassifier.config.property.ClassifyingDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.trackfilter.TrackFilterI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

//TODO: refactor with test
@Component
@RequiredArgsConstructor
public final class TrackSimplifier {
    private final TrackFilterI filter;
    private final ClassifyingDistanceProperty property;

    public Track simplify(final Track track) {
        final List<TrackPoint> filteredPoints = filterPoints(track);
        return new Track(filteredPoints);
    }

    @SuppressWarnings("unchecked")
    private List<TrackPoint> filterPoints(final Track track) {
        return (List<TrackPoint>) filter.filter(track.getPoints(), property.getTrackSimplifyEpsilon());
    }
}
