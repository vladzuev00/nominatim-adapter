package by.aurorasoft.distanceclassifier.service.distanceclassifying.cache;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.geometrypreparer.CityGeometryPreparer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static java.util.stream.Stream.empty;

@Component
public class CityGeometryCacheFactory {
    private final CityService cityService;
    private final CityGeometryPreparer geometryPreparer;
    private final boolean shouldBeFilled;

    public CityGeometryCacheFactory(final CityService cityService,
                                    final CityGeometryPreparer geometryPreparer,
                                    @Value("${distance-classifying.load-city-geometries-on-start-app}") final boolean shouldBeFilled) {
        this.cityService = cityService;
        this.geometryPreparer = geometryPreparer;
        this.shouldBeFilled = shouldBeFilled;
    }

    @Transactional(readOnly = true)
    public CityGeometryCache create() {
        try (final Stream<CityGeometry> geometries = loadGeometries()) {
            return geometries
                    .map(geometryPreparer::prepare)
                    .collect(collectingAndThen(toUnmodifiableSet(), CityGeometryCache::new));
        }
    }

    private Stream<CityGeometry> loadGeometries() {
        return shouldBeFilled ? cityService.findGeometries() : empty();
    }
}
