package by.aurorasoft.distanceclassifier.model;

import lombok.Value;
import org.locationtech.jts.geom.prep.PreparedGeometry;

@Value
public class BoundedPreparedGeometry {
    PreparedGeometry geometry;
    PreparedGeometry boundingBox;
}
