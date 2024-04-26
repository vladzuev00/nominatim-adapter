package by.aurorasoft.distanceclassifier.service.distanceclassifying.cache;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.PreparedBoundedGeometry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.emptySet;

@Component
public final class CityGeometryCacheFactory {
    private final CityService cityService;
    private final boolean shouldBeFilled;

    public CityGeometryCacheFactory(final CityService cityService,
                                    @Value("${distance-classifying.load-city-geometries-on-start-app}") final boolean shouldBeFilled) {
        this.cityService = cityService;
        this.shouldBeFilled = shouldBeFilled;
    }

    public CityGeometryCache create() {
        final Set<PreparedBoundedGeometry> geometries = loadGeometries();
        return new CityGeometryCache(geometries);
    }

    private Set<PreparedBoundedGeometry> loadGeometries() {
        return shouldBeFilled ? cityService.findBoundedPreparedGeometries() : emptySet();
    }
}
