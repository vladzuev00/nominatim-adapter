package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.factory;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.model.CityMap;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

@Component
public final class CityMapFactory {

    public CityMap create(final Set<CityGeometry> geometries, final Geometry scannedLocation) {
        final Set<PreparedCityGeometry> preparedGeometries = prepare(geometries);
        final PreparedGeometry preparedScannedLocation = prepare(scannedLocation);
        return new CityMap(preparedGeometries, preparedScannedLocation);
    }

    private Set<PreparedCityGeometry> prepare(final Set<CityGeometry> geometries) {
        return geometries.stream()
                .map(this::prepare)
                .collect(toUnmodifiableSet());
    }

    private PreparedCityGeometry prepare(final CityGeometry source) {
        final PreparedGeometry geometry = prepare(source.getGeometry());
        final PreparedGeometry boundingBox = prepare(source.getBoundingBox());
        return new PreparedCityGeometry(geometry, boundingBox);
    }

    private PreparedGeometry prepare(final Geometry geometry) {
        return PreparedGeometryFactory.prepare(geometry);
    }
}
