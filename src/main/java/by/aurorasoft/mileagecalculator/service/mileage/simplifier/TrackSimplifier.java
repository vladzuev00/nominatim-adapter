package by.aurorasoft.mileagecalculator.service.mileage.simplifier;

import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.model.TrackPoint;
import by.nhorushko.trackfilter.TrackFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class TrackSimplifier {
    private final TrackFilter filter;
    private final double epsilon;

    public TrackSimplifier(final TrackFilter filter, @Value("${track-simplifier.epsilon}") final double epsilon) {
        this.filter = filter;
        this.epsilon = epsilon;
    }

    public Track simplify(final Track track) {
        final List<TrackPoint> filteredPoints = filterPoints(track);
        return new Track(filteredPoints);
    }

    @SuppressWarnings("unchecked")
    private List<TrackPoint> filterPoints(final Track track) {
        //TODO
        return null;
//        return (List<TrackPoint>) filter.filter(track.getPoints(), epsilon);
    }
}
