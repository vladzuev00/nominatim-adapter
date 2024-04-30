package by.aurorasoft.distanceclassifier.service.distanceclassifying.pointclassifier;

import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class TrackPointLocator {
    private final GeometryService geometryService;
    private final Set<PreparedCityGeometry> cityGeometries;
    private final int citySpeedThreshold;

    public boolean isCity(final TrackPoint point) {
        return isExactCity(point) || (isUnknownLocation(point) && point.getSpeed() <= citySpeedThreshold);
    }

    private boolean isExactCity(final TrackPoint point) {
        return geometryService.isAnyGeometryContain(cityGeometries, point);
    }

    private boolean isUnknownLocation(final TrackPoint point) {
        return !geometryService.isAnyBoundingBoxContain(cityGeometries, point);
    }
}
