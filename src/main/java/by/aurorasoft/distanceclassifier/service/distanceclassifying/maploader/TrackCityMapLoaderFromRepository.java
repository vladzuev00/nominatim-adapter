package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.crud.service.ScannedLocationService;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.preparer.GeometryPreparer;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.simplifier.TrackSimplifier;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

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
    protected Set<PreparedCityGeometry> loadCityGeometries(LineString line) {
        try (final Stream<CityGeometry> geometries = cityService.findIntersectedCityGeometries(line)) {
            return geometries.map(geometryPreparer::prepare).collect(toUnmodifiableSet());
        }
    }

    @Override
    protected PreparedGeometry loadScannedGeometry() {
        final Geometry geometry = scannedLocationService.get().getGeometry();
        return geometryPreparer.prepare(geometry);
    }
}
