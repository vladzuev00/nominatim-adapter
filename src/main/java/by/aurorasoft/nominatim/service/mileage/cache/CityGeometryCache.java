package by.aurorasoft.nominatim.service.mileage.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.prep.PreparedGeometry;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public final class CityGeometryCache {
    private final Map<PreparedGeometry, PreparedGeometry> geometriesByBoundingBoxes;
}
