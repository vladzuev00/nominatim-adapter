package by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator;

import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.pointlocator.TrackPointLocator;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.pointlocator.TrackPointLocatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ClassifiedDistanceAccumulatorFactory {
    private final TrackPointLocatorFactory pointLocatorFactory;

    public ClassifiedDistanceAccumulator create(final Track track, final int urbanSpeedThreshold) {
        final TrackPointLocator pointLocator = pointLocatorFactory.create(track, urbanSpeedThreshold);
        return new ClassifiedDistanceAccumulator(pointLocator);
    }
}
