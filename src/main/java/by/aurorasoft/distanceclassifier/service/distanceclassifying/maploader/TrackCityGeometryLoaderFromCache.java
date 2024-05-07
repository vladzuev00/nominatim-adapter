package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader;

import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.cache.CityGeometryCache;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.simplifier.TrackSimplifier;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

@Component
@ConditionalOnProperty(prefix = "distance-classifying", name = "load-city-map-on-start-app", havingValue = "true")
public class TrackCityGeometryLoaderFromCache extends TrackCityMapLoader {
    private final CityGeometryCache cache;

    public TrackCityGeometryLoaderFromCache(final TrackSimplifier trackSimplifier,
                                            final GeometryService geometryService,
                                            final CityGeometryCache cache) {
        super(trackSimplifier, geometryService);
        this.cache = cache;
    }

    @Override
    protected Set<PreparedCityGeometry> loadCityGeometries(final LineString line) {
        return cache.getGeometries()
                .stream()
                .filter(geometry -> geometry.getBoundingBox().intersects(line))
                .collect(toUnmodifiableSet());
    }

    @Override
    protected PreparedGeometry loadScannedLocation() {
        return null;
    }
}
