package by.aurorasoft.nominatim.service.mileage;

import by.aurorasoft.nominatim.model.MileagePercentage;
import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import by.aurorasoft.nominatim.service.geometry.GeometryService;
import by.aurorasoft.nominatim.service.mileage.loader.TrackCityGeometryLoader;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

@Service
@RequiredArgsConstructor
public final class MileagePercentageCalculatingService {
    private final TrackCityGeometryLoader trackCityGeometryLoader;
    private final GeometryService geometryService;
    private final DistanceCalculator distanceCalculator;

    public MileagePercentage calculate(final Track track, final DistanceCalculatorSettings settings) {
        final List<PreparedGeometry> cityGeometries = loadCityGeometries(track);
        final Mileage mileage = calculateMileage(track, cityGeometries, settings);
        return calculate(mileage);
    }

    private List<PreparedGeometry> loadCityGeometries(final Track track) {
        return !track.getPoints().isEmpty() ? trackCityGeometryLoader.load(track) : emptyList();
    }

    private Mileage calculateMileage(final Track track,
                                     final List<PreparedGeometry> cityGeometries,
                                     final DistanceCalculatorSettings settings) {
        return range(0, getSliceCount(track))
                .parallel()
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
        return distanceCalculator.calculateDistance(slice.first, slice.second, settings);
    }

    private static MileagePercentage calculate(final Mileage mileage) {
        final double total = mileage.urban + mileage.country;
        final double urbanPercentage = mileage.urban / total;
        final double countryPercentage = mileage.country / total;
        return new MileagePercentage(urbanPercentage, countryPercentage / total);
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
