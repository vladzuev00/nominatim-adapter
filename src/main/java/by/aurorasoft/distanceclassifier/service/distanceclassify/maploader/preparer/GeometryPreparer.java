package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.preparer;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.springframework.stereotype.Component;

@Component
public final class GeometryPreparer {

    public PreparedGeometry prepare(final Geometry source) {
        return PreparedGeometryFactory.prepare(source);
    }

    public PreparedCityGeometry prepare(final CityGeometry source) {
        final PreparedGeometry geometry = prepare(source.getGeometry());
        final PreparedGeometry boundingBox = prepare(source.getBoundingBox());
        return new PreparedCityGeometry(geometry, boundingBox);
    }
}
