package by.aurorasoft.distanceclassifier.service.distanceclassifying;

import by.aurorasoft.distanceclassifier.model.MileagePercentage;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.loader.TrackCityGeometryLoader;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import by.nhorushko.classifieddistance.ClassifiedDistance;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public final class ClassifyingDistanceService {
    private final TrackCityGeometryLoader trackCityGeometryLoader;
    private final GeometryService geometryService;

    public ClassifiedDistanceStorage classify(final Track track, final int urbanSpeedThreshold) {
        final TrackPointClassifier pointClassifier = createPointClassifier(track, urbanSpeedThreshold);
        return null;
//        return track.getPoints()
//                .stream()
//                .map(point -> new ClassifiedDistance())
//        return track.getPoints()
//                .stream()
//                .collect(
//                        teeing(
//                                teeing(summingDouble())
//                        )
//                );
//        final Set<PreparedBoundedGeometry> cityGeometries = trackCityGeometryLoader.load(track);
//        return track.getPoints()
//                .stream()
//                .
//        return range(0, getSliceCount(track))
//                .mapToObj(i -> getSlice(track, i))
//                .collect(
//                        collectingAndThen(
//                                partitioningBy(
//                                        slice -> isUrbanSlice(slice, cityGeometries, urbanSpeedThreshold),
//                                        reducing(ClassifiedDistanceStorage::plus)
//                                ),
//                                mileagesByUrban -> new Mileage(mileagesByUrban.get(true), mileagesByUrban.get(false))
//                        )
//                );
    }

    private TrackPointClassifier createPointClassifier(final Track track, final int urbanSpeedThreshold) {
        final Set<PreparedCityGeometry> cityGeometries = trackCityGeometryLoader.load(track);
        return new TrackPointClassifier(geometryService, cityGeometries, urbanSpeedThreshold);
    }

//    private boolean isUrban(final TrackPoint point,
//                            final Set<PreparedBoundedGeometry> cityGeometries,
//                            final int urbanSpeedThreshold){
//        PreparedGeometry a;
//        a.disjoint()
//        if(geometryService.isAnyContain())
//    }

    public MileagePercentage TEMPcalculate(final Track track, final DistanceCalculatorSettings settings) {
//        final List<PreparedGeometry> cityGeometries = trackCityGeometryLoader.load(track);
//        final Mileage mileage = calculateMileage(track, cityGeometries, settings);
//        return calculate(mileage);
        return null;
    }

//    private Mileage calculateMileage(final Track track,
//                                     final List<PreparedGeometry> cityGeometries,
//                                     final DistanceCalculatorSettings settings) {
//        return range(0, getSliceCount(track))
//                .mapToObj(i -> getSlice(track, i))
//                .collect(
//                        collectingAndThen(
//                                partitioningBy(
//                                        slice -> geometryService.isAnyContain(cityGeometries, slice.second),
//                                        summingDouble(slice -> findLength(slice, settings))
//                                ),
//                                mileagesByUrban -> new Mileage(mileagesByUrban.get(true), mileagesByUrban.get(false))
//                        )
//                );
//    }

//    static final class ClassifiedDistanceAccumulator {
//        private final TrackPointClassifier classifier;
//        private double
//    }

    static final class ClassifiedDistanceAccumulator {

    }

    @RequiredArgsConstructor
    static final class TrackPointClassifier {
        private final GeometryService geometryService;
        private final Set<PreparedCityGeometry> cityGeometries;
        private final int urbanSpeedThreshold;

        public boolean isUrban(final TrackPoint point) {
            return isLocatedInCity(point) || (isUnknownLocation(point) && point.getSpeed() <= urbanSpeedThreshold);
        }

        private boolean isLocatedInCity(final TrackPoint point) {
            return geometryService.isAnyGeometryContain(cityGeometries, point);
        }

        private boolean isUnknownLocation(final TrackPoint point) {
            return !geometryService.isAnyBoundingBoxContain(cityGeometries, point);
        }
    }
}
