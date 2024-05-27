package by.aurorasoft.distanceclassifier.service.distanceclassify.accumulator.pointlocator;

import by.aurorasoft.distanceclassifier.model.CityMap;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TrackPointLocator {
    private final GeometryService geometryService;
    private final CityMap cityMap;
    private final int citySpeedThreshold;

    public boolean isCity(final TrackPoint point) {
        return isAnyCityContain(point) || (point.getSpeed() <= citySpeedThreshold && isUnknownLocation(point));
    }

    private boolean isAnyCityContain(final TrackPoint point) {
        return geometryService.isAnyContain(cityMap.getCityGeometries(), point);
    }

    private boolean isUnknownLocation(final TrackPoint point) {
        return !geometryService.isContain(cityMap.getScannedGeometry(), point);
    }
}
