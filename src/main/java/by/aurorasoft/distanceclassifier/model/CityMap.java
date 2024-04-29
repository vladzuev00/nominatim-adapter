package by.aurorasoft.distanceclassifier.model;

import lombok.Value;
import org.locationtech.jts.geom.prep.PreparedGeometry;

@Value
public class CityMap {
    PreparedGeometry unionGeometries;
    PreparedGeometry unionBoundingBoxes;
}
