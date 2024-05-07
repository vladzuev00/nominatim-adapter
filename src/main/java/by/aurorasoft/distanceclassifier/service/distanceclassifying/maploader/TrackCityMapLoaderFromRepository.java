package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.crud.service.ScannedLocationService;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.preparer.GeometryPreparer;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.simplifier.TrackSimplifier;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@ConditionalOnProperty(prefix = "distance-classifying", name = "cache-geometries", havingValue = "false")
public class TrackCityMapLoaderFromRepository extends TrackCityMapLoader {
    private final CityService cityService;
    private final ScannedLocationService scannedLocationService;
    private final GeometryPreparer geometryPreparer;

    public TrackCityMapLoaderFromRepository(final TrackSimplifier trackSimplifier,
                                            final GeometryService geometryService,
                                            final CityService cityService,
                                            final ScannedLocationService scannedLocationService,
                                            final GeometryPreparer geometryPreparer) {
        super(trackSimplifier, geometryService);
        this.cityService = cityService;
        this.scannedLocationService = scannedLocationService;
        this.geometryPreparer = geometryPreparer;
    }

    @Override
    protected Stream<PreparedGeometry> loadCityGeometries(final LineString line) {
        return cityService.findIntersectedCityGeometries(line)
                .map(CityGeometry::getGeometry)
                .map(geometryPreparer::prepare);
    }

    @Override
    protected PreparedGeometry loadScannedGeometry() {
        final Geometry geometry = scannedLocationService.get().getGeometry();
        return geometryPreparer.prepare(geometry);
    }
}
