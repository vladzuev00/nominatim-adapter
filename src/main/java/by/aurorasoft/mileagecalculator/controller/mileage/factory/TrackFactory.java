package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.TEMPMileageRequest;
import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.model.TrackPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class TrackFactory {
    private final TEMPTrackPointFactory pointFactory;

    public Track create(final TEMPMileageRequest request) {
        final List<TrackPoint> points = createPoints(request);
        return new Track(points);
    }

    private List<TrackPoint> createPoints(final TEMPMileageRequest request) {
        return request.getTrackPoints()
                .stream()
                .map(pointFactory::create)
                .toList();
    }
}
