package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.factory;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.crud.service.ScannedLocationService;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.preparer.GeometryPreparer;
import org.locationtech.jts.geom.Geometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@ConditionalOnProperty(prefix = "distance-classifying", name = "cache-geometries", havingValue = "true")
public class FilledGeometryCacheFactory extends GeometryCacheFactory {
    private final CityService cityService;
    private final ScannedLocationService scannedLocationService;

    public FilledGeometryCacheFactory(final GeometryPreparer geometryPreparer,
                                      final CityService cityService,
                                      final ScannedLocationService scannedLocationService) {
        super(geometryPreparer);
        this.cityService = cityService;
        this.scannedLocationService = scannedLocationService;
    }

    @Override
    protected Stream<CityGeometry> getCityGeometries() {
        return cityService.findCityGeometries();
    }

    @Override
    protected Geometry getScannedGeometry() {
        return scannedLocationService.get().getGeometry();
    }
}
