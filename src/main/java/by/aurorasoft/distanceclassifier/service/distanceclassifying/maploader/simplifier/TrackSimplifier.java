package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.simplifier;

import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.trackfilter.TrackFilterI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class TrackSimplifier {
    private final TrackFilterI filter;
    private final double epsilon;

    public TrackSimplifier(final TrackFilterI filter, @Value("${track-simplifier.epsilon}") final double epsilon) {
        this.filter = filter;
        this.epsilon = epsilon;
    }

    public Track simplify(final Track track) {
        final List<TrackPoint> filteredPoints = filterPoints(track);
        return new Track(filteredPoints);
    }

    @SuppressWarnings("unchecked")
    private List<TrackPoint> filterPoints(final Track track) {
        return (List<TrackPoint>) filter.filter(track.getPoints(), epsilon);
    }
}
