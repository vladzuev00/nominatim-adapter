package by.aurorasoft.distanceclassifier.service.distanceclassify.accumulator.pointlocator;

import by.aurorasoft.distanceclassifier.model.CityMap;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.TrackCityMapLoader;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class TrackPointLocatorFactory {
    private final TrackCityMapLoader trackCityMapLoader;
    private final GeometryService geometryService;

    public TrackPointLocator create(final Track track, final int citySpeedThreshold) {
        final CityMap cityMap = trackCityMapLoader.load(track);
        return new TrackPointLocator(geometryService, cityMap, citySpeedThreshold);
    }
}
