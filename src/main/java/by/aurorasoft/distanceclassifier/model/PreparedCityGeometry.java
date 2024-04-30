package by.aurorasoft.distanceclassifier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.locationtech.jts.geom.prep.PreparedGeometry;

@Value
@AllArgsConstructor
@Builder
public class PreparedCityGeometry {
    PreparedGeometry geometry;
    PreparedGeometry boundingBox;
}
