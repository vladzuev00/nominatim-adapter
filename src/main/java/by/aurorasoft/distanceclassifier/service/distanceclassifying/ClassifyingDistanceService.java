package by.aurorasoft.distanceclassifier.service.distanceclassifying;

import by.aurorasoft.distanceclassifier.model.MileagePercentage;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.loader.TrackCityGeometryLoader;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public final class ClassifyingDistanceService {
    private final TrackCityGeometryLoader trackCityGeometryLoader;
    private final GeometryService geometryService;

    public ClassifiedDistanceStorage classify(final Track track, final int urbanSpeedThreshold) {
        final Set<PreparedCityGeometry> cityGeometries = trackCityGeometryLoader.load(track);
        return null;

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

    private boolean isUrbanPoint(final TrackPoint point,
                                 final Set<PreparedCityGeometry> geometries,
                                 final int speedThreshold) {
        return isLocatedInCity(point, geometries) || (isUnknownLocation(point, geometries) && point.getSpeed() <= speedThreshold);
    }

    private boolean isLocatedInCity(final TrackPoint point, final Set<PreparedCityGeometry> geometries) {
        return geometryService.isAnyGeometryContain(geometries, point);
    }

    private boolean isUnknownLocation(final TrackPoint point, final Set<PreparedCityGeometry> geometries) {
        return !geometryService.isAnyBoundingBoxContain(geometries, point);
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

    private static int getSliceCount(final Track track) {
        return track.getPoints().size() - 1;
    }

    private static TrackSlice getSlice(final Track track, final int index) {
        return new TrackSlice(track.getPoint(index), track.getPoint(index + 1));
    }

    @Value
    private static class TrackSlice {
        TrackPoint first;
        TrackPoint second;
    }
}
