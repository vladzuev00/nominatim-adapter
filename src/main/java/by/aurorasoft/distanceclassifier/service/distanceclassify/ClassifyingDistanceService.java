package by.aurorasoft.distanceclassifier.service.distanceclassify;

import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassify.accumulator.ClassifiedDistanceAccumulator;
import by.aurorasoft.distanceclassifier.service.distanceclassify.accumulator.ClassifiedDistanceAccumulatorFactory;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.factory.SkippingTrackPointIteratorFactory;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class ClassifyingDistanceService {
    private final ClassifiedDistanceAccumulatorFactory distanceAccumulatorFactory;
    private final SkippingTrackPointIteratorFactory pointIteratorFactory;

    public ClassifiedDistanceStorage classify(final Track track, final int urbanSpeedThreshold) {
        final ClassifiedDistanceAccumulator accumulator = distanceAccumulatorFactory.create(track, urbanSpeedThreshold);
        pointIteratorFactory.create(track).forEachRemaining(accumulator::accumulate);
        return accumulator.get();
    }
}
