package by.aurorasoft.distanceclassifier.controller.classifydistance.factory;

import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest;
import by.aurorasoft.distanceclassifier.model.Track;
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
