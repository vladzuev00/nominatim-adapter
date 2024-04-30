package by.aurorasoft.distanceclassifier.service.distanceclassifying.pointlocator;

import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.geometryloader.TrackCityGeometryLoader;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public final class TrackPointLocatorFactory {
    private final TrackCityGeometryLoader trackCityGeometryLoader;
    private final GeometryService geometryService;

    public TrackPointLocator create(final Track track, final int citySpeedThreshold) {
        final Set<PreparedCityGeometry> cityGeometries = trackCityGeometryLoader.load(track);
        return new TrackPointLocator(geometryService, cityGeometries, citySpeedThreshold);
    }
}
