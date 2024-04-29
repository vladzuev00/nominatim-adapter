package by.aurorasoft.distanceclassifier.service.distanceclassifying.citymap.factory;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.CityMapView;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.citymap.CityMap;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;

@Component
public final class CityMapFactory {
    private final CityService cityService;
    private final boolean shouldLoadOnStartApp;

    public CityMapFactory(final CityService cityService,
                          @Value("${distance-classifying.load-city-map-on-start-app}") final boolean shouldLoadOnStartApp) {
        this.cityService = cityService;
        this.shouldLoadOnStartApp = shouldLoadOnStartApp;
    }

    public CityMap create() {
        return shouldLoadOnStartApp ? load() : createEmptyMap();
    }

    private CityMap load() {
        final CityMapView view = cityService.loadCityMap();
        final PreparedGeometry unionGeometries = getPreparedGeometry(view, CityMapView::unionGeometries);
        final PreparedGeometry unionBoundingBoxes = getPreparedGeometry(view, CityMapView::unionBoundingBoxes);
        return new CityMap(unionGeometries, unionBoundingBoxes);
    }

    private static PreparedGeometry getPreparedGeometry(final CityMapView view,
                                                        final Function<CityMapView, Geometry> getter) {
        final Geometry geometry = getter.apply(view);
        return prepare(geometry);
    }

    private static CityMap createEmptyMap() {
        return new CityMap(null, null);
    }
}
