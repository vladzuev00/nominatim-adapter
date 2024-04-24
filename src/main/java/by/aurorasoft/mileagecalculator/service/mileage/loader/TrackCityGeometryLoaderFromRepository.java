package by.aurorasoft.mileagecalculator.service.mileage.loader;

import by.aurorasoft.mileagecalculator.crud.service.CityService;
import by.aurorasoft.mileagecalculator.service.geometry.GeometryService;
import by.aurorasoft.mileagecalculator.service.mileage.simplifier.TrackSimplifier;
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
