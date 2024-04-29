package by.aurorasoft.distanceclassifier.service.distanceclassifying.loader;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.PreparedBoundedGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.simplifier.TrackSimplifier;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.locationtech.jts.geom.LineString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConditionalOnProperty(prefix = "distance-classifying", name = "load-city-geometries-on-start-app", havingValue = "false")
public final class TrackCityGeometryLoaderFromRepository extends TrackCityGeometryLoader {
    private final CityService cityService;

    public TrackCityGeometryLoaderFromRepository(final TrackSimplifier trackSimplifier,
                                                 final GeometryService geometryService,
                                                 final CityService cityService) {
        super(trackSimplifier, geometryService);
        this.cityService = cityService;
    }

    @Override
    protected Set<PreparedBoundedGeometry> loadInternal(final LineString line) {
        return null;
//        return cityService.findBoundedPreparedGeometries(line);
    }
}
