package by.aurorasoft.distanceclassifier.service.distanceclassifying.loader;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.simplifier.TrackSimplifier;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(prefix = "mileage-calc", name = "load-city-geometries-on-start-app", havingValue = "false")
public final class TrackCityGeometryLoaderFromRepository extends TrackCityGeometryLoader {
    private final CityService cityService;

    public TrackCityGeometryLoaderFromRepository(final TrackSimplifier trackSimplifier,
                                                 final GeometryService geometryService,
                                                 final CityService cityService) {
        super(trackSimplifier, geometryService);
        this.cityService = cityService;
    }

    @Override
    protected List<PreparedGeometry> load(final LineString line) {
        return cityService.findIntersectedPreparedGeometries(line);
    }
}
