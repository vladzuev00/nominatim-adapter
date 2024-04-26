package by.aurorasoft.distanceclassifier.crud.repository.view;

import org.locationtech.jts.geom.Geometry;

public interface CityGeometryMapView {
    Geometry getUnionGeometries();

    Geometry getUnionBoundingBoxes();
}
