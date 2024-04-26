package by.aurorasoft.distanceclassifier.service.distanceclassifying.cache;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Component
public final class CityGeometryCacheFactory {
    private final CityService cityService;
    private final boolean shouldBeFilled;

    public CityGeometryCacheFactory(final CityService cityService,
                                    @Value("${mileage-calc.load-city-geometries-on-start-app}") final boolean shouldBeFilled) {
        this.cityService = cityService;
        this.shouldBeFilled = shouldBeFilled;
    }

    public CityGeometryCache create() {
        final var geometriesByBoundingBoxes = loadGeometries();
        return new CityGeometryCache(geometriesByBoundingBoxes);
    }

    private Map<PreparedGeometry, PreparedGeometry> loadGeometries() {
        return shouldBeFilled ? cityService.findPreparedGeometriesByPreparedBoundingBoxes() : emptyMap();
    }
}
