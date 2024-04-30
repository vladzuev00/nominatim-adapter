package by.aurorasoft.distanceclassifier.service.distanceclassifying.loader;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.geometrypreparer.CityGeometryPreparer;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.simplifier.TrackSimplifier;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.locationtech.jts.geom.LineString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

@Component
@ConditionalOnProperty(prefix = "distance-classifying", name = "load-city-geometries-on-start-app", havingValue = "false")
public final class TrackCityGeometryLoaderFromRepository extends TrackCityGeometryLoader {
    private final CityService cityService;
    private final CityGeometryPreparer geometryPreparer;

    public TrackCityGeometryLoaderFromRepository(final TrackSimplifier trackSimplifier,
                                                 final GeometryService geometryService,
                                                 final CityService cityService,
                                                 final CityGeometryPreparer geometryPreparer) {
        super(trackSimplifier, geometryService);
        this.cityService = cityService;
        this.geometryPreparer = geometryPreparer;
    }

    @Override
    @Transactional(readOnly = true)
    protected Set<PreparedCityGeometry> loadInternal(final LineString line) {
        try (final Stream<CityGeometry> geometries = cityService.findIntersectedGeometries(line)) {
            return geometries.map(geometryPreparer::prepare).collect(toUnmodifiableSet());
        }
    }
}
