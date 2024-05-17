package by.aurorasoft.distanceclassifier.service.distanceclassifying;

import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.ClassifiedDistanceAccumulator;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.ClassifiedDistanceAccumulatorFactory;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.factory.ConnectingTrackPointIteratorFactory;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class ClassifyingDistanceService {
    private final ClassifiedDistanceAccumulatorFactory distanceAccumulatorFactory;
    private final ConnectingTrackPointIteratorFactory unionPointIteratorFactory;

    public ClassifiedDistanceStorage classify(final Track track, final int urbanSpeedThreshold) {
        final ClassifiedDistanceAccumulator accumulator = distanceAccumulatorFactory.create(track, urbanSpeedThreshold);
        unionPointIteratorFactory.create(track).forEachRemaining(accumulator::accumulate);
        return accumulator.get();
    }
}
