package by.aurorasoft.nominatim.service.mileage;

import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import by.aurorasoft.nominatim.rest.model.Mileage;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.IntStream.rangeClosed;

@Service
@RequiredArgsConstructor
public final class MileageCalculatingService {
    private final TrackCityGeometryLoader trackCityGeometryLoader;
    private final GeometryService geometryService;
    private final DistanceCalculator distanceCalculator;

    public Mileage calculate(final Track track, final DistanceCalculatorSettings settings) {
        final Map<Boolean, Double> mileagesByUrban = findMileagesByUrban(track, settings);
        final double urban = mileagesByUrban.get(true);
        final double country = mileagesByUrban.get(false);
        return new Mileage(urban, country);
    }

    private Map<Boolean, Double> findMileagesByUrban(final Track track, final DistanceCalculatorSettings settings) {
        final List<PreparedGeometry> cityGeometries = trackCityGeometryLoader.load(track);
        final int penultimatePointIndex = track.getPoints().size() - 2;
        return rangeClosed(0, penultimatePointIndex)
                .mapToObj(i -> getSlice(track, i, cityGeometries))
                .collect(partitioningBy(TrackSlice::isUrban, summingDouble(slice -> calculateDistance(slice, settings))));
    }

    private TrackSlice getSlice(final Track track, final int index, final List<PreparedGeometry> cityGeometries) {
        final TrackPoint first = track.getPoint(index);
        final TrackPoint second = track.getPoint(index + 1);
        final boolean urban = geometryService.isAnyContain(cityGeometries, second);
        return new TrackSlice(first, second, urban);
    }

    private double calculateDistance(final TrackSlice slice, final DistanceCalculatorSettings settings) {
        return distanceCalculator.calculateDistance(slice.first, slice.second, settings);
    }

    @Value
    private static class TrackSlice {
        TrackPoint first;
        TrackPoint second;
        boolean urban;
    }
}
