package by.aurorasoft.nominatim.controller.mileage.factory;

import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest;
import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class TrackFactory {
    private final TrackPointFactory pointFactory;

    public Track create(final MileageRequest request) {
        final List<TrackPoint> points = createPoints(request);
        return new Track(points);
    }

    private List<TrackPoint> createPoints(final MileageRequest request) {
        return request.getTrackPoints()
                .stream()
                .map(pointFactory::create)
                .toList();
    }
}
