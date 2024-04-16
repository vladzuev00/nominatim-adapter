package by.aurorasoft.nominatim.service.mileage.geometryloader;

import by.aurorasoft.nominatim.service.mileage.cache.CityGeometryCache;
import by.aurorasoft.nominatim.service.geometry.GeometryService;
import by.aurorasoft.nominatim.service.mileage.tracksimplifer.TrackSimplifier;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map.Entry;

@Component
@ConditionalOnProperty(prefix = "mileage-calc", name = "load-city-geometries-on-start-app", havingValue = "true")
public final class TrackCityGeometryLoaderFromCache extends TrackCityGeometryLoader {
    private final CityGeometryCache geometryCache;

    public TrackCityGeometryLoaderFromCache(final TrackSimplifier trackSimplifier,
                                            final GeometryService geometryService,
                                            final CityGeometryCache geometryCache) {
        super(trackSimplifier, geometryService);
        this.geometryCache = geometryCache;
    }

    @Override
    protected List<PreparedGeometry> load(final LineString line) {
        return geometryCache.getGeometriesByBoundingBoxes()
                .entrySet()
                .stream()
                .filter(geometryByBoundingBox -> geometryByBoundingBox.getKey().intersects(line))
                .map(Entry::getValue)
                .toList();
    }
}
