package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.crud.service.ScannedLocationService;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.preparer.GeometryPreparer;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

@Component
public class GeometryCacheFactory {
    private final CityService cityService;
    private final ScannedLocationService scannedLocationService;
    private final GeometryPreparer geometryPreparer;
    private final boolean shouldBeFilled;

    public GeometryCacheFactory(final CityService cityService,
                                final ScannedLocationService scannedLocationService,
                                final GeometryPreparer geometryPreparer,
                                @Value("${distance-classifying.cache-geometries}") final boolean shouldBeFilled) {
        this.cityService = cityService;
        this.scannedLocationService = scannedLocationService;
        this.geometryPreparer = geometryPreparer;
        this.shouldBeFilled = shouldBeFilled;
    }

    @Transactional(readOnly = true)
    public GeometryCache create() {
        return shouldBeFilled ? createFilled() : createEmpty();
    }

    private GeometryCache createFilled() {

    }

    private GeometryCache createEmpty() {

    }

    private GeometryCache create(final Supplier<Set<PreparedCityGeometry>> cityGeometriesSupplier,
                                 final Supplier<PreparedGeometry> scannedGeometrySupplier) {
        final Set<PreparedCityGeometry> cityGeometries = cityGeometriesSupplier.get();
        final PreparedGeometry scannedGeometry = scannedGeometrySupplier.get();
        return new GeometryCache(cityGeometries, scannedGeometry);
    }

    private Set<PreparedCityGeometry> loadCityGeometries() {
        try (final Stream<CityGeometry> geometries = cityService.findCityGeometries()) {
            return geometries.map(geometryPreparer::prepare).collect(toUnmodifiableSet());
        }
    }
}
