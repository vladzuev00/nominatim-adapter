package by.aurorasoft.nominatim.service.mileage.cache;

import lombok.Getter;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

//@RequiredArgsConstructor
@Getter
@Component
public final class CityGeometryCache {
    private final Map<PreparedGeometry, PreparedGeometry> geometriesByBoundingBoxes = Collections.emptyMap();
}
