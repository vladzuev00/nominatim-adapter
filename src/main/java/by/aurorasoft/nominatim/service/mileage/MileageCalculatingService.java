package by.aurorasoft.nominatim.service.mileage;

import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import by.aurorasoft.nominatim.rest.model.Mileage;
import by.aurorasoft.nominatim.service.mileage.geometryloader.TrackCityGeometryLoader;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

@Service
@RequiredArgsConstructor
public final class MileageCalculatingService {
    private final TrackCityGeometryLoader trackCityGeometryLoader;
    private final GeometryService geometryService;
    private final DistanceCalculator distanceCalculator;

    public Mileage calculate(final Track track, final DistanceCalculatorSettings settings) {
        final List<PreparedGeometry> cityGeometries = trackCityGeometryLoader.load(track);
        return range(0, getSliceCount(track))
                .mapToObj(i -> getSlice(track, i))
                .collect(
                        collectingAndThen(
                                partitioningBy(
                                        slice -> geometryService.isAnyContain(cityGeometries, slice.second),
                                        summingDouble(slice -> calculateDistance(slice, settings))
                                ),
                                mileagesByUrban -> new Mileage(mileagesByUrban.get(true), mileagesByUrban.get(false))
                        )
                );
    }

    private int getSliceCount(final Track track) {
        return track.getPoints().size() - 1;
    }

    private TrackSlice getSlice(final Track track, final int index) {
        return new TrackSlice(track.getPoint(index), track.getPoint(index + 1));
    }

    private double calculateDistance(final TrackSlice slice, final DistanceCalculatorSettings settings) {
        return distanceCalculator.calculateDistance(slice.first, slice.second, settings);
    }

    @Value
    private static class TrackSlice {
        TrackPoint first;
        TrackPoint second;
    }
}
