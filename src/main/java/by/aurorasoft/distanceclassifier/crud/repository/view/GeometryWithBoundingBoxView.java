package by.aurorasoft.distanceclassifier.crud.repository.view;

import org.locationtech.jts.geom.Geometry;

public interface GeometryWithBoundingBoxView {
    Geometry getGeometry();

    Geometry getBoundingBox();
}
