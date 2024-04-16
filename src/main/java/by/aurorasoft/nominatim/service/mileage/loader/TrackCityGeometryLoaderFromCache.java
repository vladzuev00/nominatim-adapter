package by.aurorasoft.nominatim.service.mileage.loader;

import by.aurorasoft.nominatim.service.mileage.cache.CityGeometryCache;
import by.aurorasoft.nominatim.service.geometry.GeometryService;
import by.aurorasoft.nominatim.service.mileage.simplifier.TrackSimplifier;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map.Entry;

@Component
@ConditionalOnProperty(prefix = "mileage-calc", name = "load-city-geometries-on-start-app", havingValue = "true")
public final class TrackCityGeometryLoaderFromCache extends TrackCityGeometryLoader {
    private final CityGeometryCache cache;

    public TrackCityGeometryLoaderFromCache(final TrackSimplifier trackSimplifier,
                                            final GeometryService geometryService,
                                            final CityGeometryCache cache) {
        super(trackSimplifier, geometryService);
        this.cache = cache;
    }

    @Override
    protected List<PreparedGeometry> load(final LineString line) {
        return cache.getGeometriesByBoundingBoxes()
                .entrySet()
                .stream()
                .filter(geometryByBoundingBox -> geometryByBoundingBox.getKey().intersects(line))
                .map(Entry::getValue)
                .toList();
    }
}