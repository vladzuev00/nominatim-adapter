package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.factory;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.GeometryCache;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.preparer.GeometryPreparer;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

@RequiredArgsConstructor
public abstract class GeometryCacheFactory {
    private final GeometryPreparer geometryPreparer;

    @Transactional(readOnly = true)
    public GeometryCache create() {
        final Set<PreparedCityGeometry> cityGeometries = getPreparedCityGeometries();
        final PreparedGeometry scannedGeometry = getPreparedScannedGeometry();
        return new GeometryCache(cityGeometries, scannedGeometry);
    }

    protected abstract Stream<CityGeometry> getCityGeometries();

    protected abstract Geometry getScannedGeometry();

    private Set<PreparedCityGeometry> getPreparedCityGeometries() {
        try (final Stream<CityGeometry> geometries = getCityGeometries()) {
            return geometries.map(geometryPreparer::prepare).collect(toUnmodifiableSet());
        }
    }

    private PreparedGeometry getPreparedScannedGeometry() {
        return geometryPreparer.prepare(getScannedGeometry());
    }
}
