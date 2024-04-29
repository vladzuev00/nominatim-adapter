package by.aurorasoft.distanceclassifier.service.distanceclassifying.cache;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
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
    private final boolean shouldBeFilled;

    public CityGeometryCacheFactory(final CityService cityService,
                                    @Value("${distance-classifying.load-city-geometries-on-start-app}") final boolean shouldBeFilled) {
        this.cityService = cityService;
        this.shouldBeFilled = shouldBeFilled;
    }

    @Transactional(readOnly = true)
    public CityGeometryCache create() {
        try (final Stream<CityGeometry> geometries = loadGeometries()) {
            return geometries
                    .map(CityGeometryCacheFactory::prepare)
                    .collect(collectingAndThen(toUnmodifiableSet(), CityGeometryCache::new));
        }
    }

    private Stream<CityGeometry> loadGeometries() {
        return shouldBeFilled ? cityService.findGeometries() : empty();
    }

    private static PreparedCityGeometry prepare(final CityGeometry cityGeometry) {
        final PreparedGeometry geometry = PreparedGeometryFactory.prepare(cityGeometry.getGeometry());
        final PreparedGeometry boundingBox = PreparedGeometryFactory.prepare(cityGeometry.getBoundingBox());
        return new PreparedCityGeometry(geometry, boundingBox);
    }
}
