package by.aurorasoft.distanceclassifier.service.distanceclassifying.cache;

import by.aurorasoft.distanceclassifier.model.BoundedPreparedGeometry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public final class CityGeometryCache {
    private final Set<BoundedPreparedGeometry> boundedGeometries;
}
