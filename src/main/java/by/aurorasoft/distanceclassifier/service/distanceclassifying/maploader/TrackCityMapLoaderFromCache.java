package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader;

import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.GeometryCache;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.simplifier.TrackSimplifier;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@ConditionalOnProperty(prefix = "distance-classifying", name = "cache-geometries", havingValue = "true")
public class TrackCityMapLoaderFromCache extends TrackCityMapLoader {
    private final GeometryCache cache;

    public TrackCityMapLoaderFromCache(final TrackSimplifier trackSimplifier,
                                       final GeometryService geometryService,
                                       final GeometryCache cache) {
        super(trackSimplifier, geometryService);
        this.cache = cache;
    }

    @Override
    protected Stream<PreparedCityGeometry> loadCityGeometries(final LineString line) {
        return cache.getCityGeometries()
                .stream()
                .filter(geometry -> geometry.getBoundingBox().intersects(line));
    }

    @Override
    protected PreparedGeometry loadScannedGeometry() {
        return cache.getScannedGeometry();
    }
}
