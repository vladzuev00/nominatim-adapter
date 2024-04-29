package by.aurorasoft.distanceclassifier.service.distanceclassifying.preparer;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.springframework.stereotype.Component;

@Component
public final class CityGeometryPreparer {

    public PreparedCityGeometry prepare(final CityGeometry cityGeometry) {
        final PreparedGeometry geometry = PreparedGeometryFactory.prepare(cityGeometry.getGeometry());
        final PreparedGeometry boundingBox = PreparedGeometryFactory.prepare(cityGeometry.getBoundingBox());
        return new PreparedCityGeometry(geometry, boundingBox);
    }
}
