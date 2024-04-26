package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.ClassifyDistanceRequest;
import by.aurorasoft.mileagecalculator.model.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public final class TrackFactory {
    private final TrackPointFactory pointFactory;

    public Track create(final ClassifyDistanceRequest request) {
        return request.getTrackPoints()
                .stream()
                .map(pointFactory::create)
                .collect(collectingAndThen(toList(), Track::new));
    }
}
