package by.aurorasoft.distanceclassifier.model;

import org.locationtech.jts.geom.Geometry;

public interface CityMapView {
    Geometry unionGeometries();

    Geometry unionBoundingBoxes();
}
