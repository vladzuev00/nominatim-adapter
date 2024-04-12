package by.aurorasoft.nominatim.model;

import lombok.Value;
import org.locationtech.jts.geom.Geometry;

@Value
public class GeometryWithBoundingBox {
    Geometry geometry;
    Geometry boundingBox;
}
