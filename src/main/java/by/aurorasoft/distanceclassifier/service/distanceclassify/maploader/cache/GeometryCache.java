package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache;

import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.prep.PreparedGeometry;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public final class GeometryCache {
    private final Set<PreparedCityGeometry> cityGeometries;
    private final PreparedGeometry scannedGeometry;
}
