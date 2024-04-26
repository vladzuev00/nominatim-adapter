package by.aurorasoft.distanceclassifier.service.distanceclassifying;

import by.aurorasoft.distanceclassifier.model.BoundedPreparedGeometry;
import by.aurorasoft.distanceclassifier.model.MileagePercentage;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.loader.TrackCityGeometryLoader;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleFunction;

import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

@Service
@RequiredArgsConstructor
public final class ClassifyingDistanceService {
    private final TrackCityGeometryLoader trackCityGeometryLoader;
    private final GeometryService geometryService;

    public ClassifiedDistanceStorage classify(final Track track, final int urbanSpeedThreshold) {
//        final Set<BoundedPreparedGeometry> cityGeometries = trackCityGeometryLoader.load(track);
//        return range(0, getSliceCount(track))
//                .mapToObj(i -> getSlice(track, i))
//                .collect(
//
//                )
        return null;
    }

    public MileagePercentage TEMPcalculate(final Track track, final DistanceCalculatorSettings settings) {
//        final List<PreparedGeometry> cityGeometries = trackCityGeometryLoader.load(track);
//        final Mileage mileage = calculateMileage(track, cityGeometries, settings);
//        return calculate(mileage);
        return null;
    }

    private Mileage calculateMileage(final Track track,
                                     final List<PreparedGeometry> cityGeometries,
                                     final DistanceCalculatorSettings settings) {
        return range(0, getSliceCount(track))
                .mapToObj(i -> getSlice(track, i))
                .collect(
                        collectingAndThen(
                                partitioningBy(
                                        slice -> geometryService.isAnyContain(cityGeometries, slice.second),
                                        summingDouble(slice -> findLength(slice, settings))
                                ),
                                mileagesByUrban -> new Mileage(mileagesByUrban.get(true), mileagesByUrban.get(false))
                        )
                );
    }

    private static int getSliceCount(final Track track) {
        return track.getPoints().size() - 1;
    }

    private static TrackSlice getSlice(final Track track, final int index) {
        return new TrackSlice(track.getPoint(index), track.getPoint(index + 1));
    }

    private double findLength(final TrackSlice slice, final DistanceCalculatorSettings settings) {
        //TODO
        return 0;
//        return distanceCalculator.calculateDistance(slice.first, slice.second, settings);
    }

    private static MileagePercentage calculate(final Mileage mileage) {
        final double urban = calculateValuePercentage(mileage, Mileage::getUrban);
        final double country = calculateValuePercentage(mileage, Mileage::getCountry);
        return new MileagePercentage(urban, country);
    }

    private static double calculateValuePercentage(final Mileage mileage, final ToDoubleFunction<Mileage> valueGetter) {
        final double value = valueGetter.applyAsDouble(mileage);
        final double total = mileage.urban + mileage.country;
        return value * 100 / total;
    }

    @Value
    private static class TrackSlice {
        TrackPoint first;
        TrackPoint second;
    }

    @Value
    private static class Mileage {
        double urban;
        double country;
    }
}
