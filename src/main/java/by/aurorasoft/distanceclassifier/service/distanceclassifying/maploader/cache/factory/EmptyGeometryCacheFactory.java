package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.factory;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.preparer.GeometryPreparer;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.locationtech.jts.geom.Geometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static java.util.stream.Stream.empty;

@Component
@ConditionalOnProperty(prefix = "distance-classifying", name = "cache-geometries", havingValue = "false")
public class EmptyGeometryCacheFactory extends GeometryCacheFactory {
    private final GeometryService geometryService;

    public EmptyGeometryCacheFactory(final GeometryPreparer geometryPreparer, final GeometryService geometryService) {
        super(geometryPreparer);
        this.geometryService = geometryService;
    }

    @Override
    protected Stream<CityGeometry> getCityGeometries() {
        return empty();
    }

    @Override
    protected Geometry getScannedGeometry() {
        return geometryService.createEmptyPolygon();
    }
}
