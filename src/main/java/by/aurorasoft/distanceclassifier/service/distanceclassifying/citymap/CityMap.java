package by.aurorasoft.distanceclassifier.service.distanceclassifying.citymap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.prep.PreparedGeometry;

@RequiredArgsConstructor
@Getter
public final class CityMap {
    private final PreparedGeometry unionGeometries;
    private final PreparedGeometry unionBoundingBoxes;
}
