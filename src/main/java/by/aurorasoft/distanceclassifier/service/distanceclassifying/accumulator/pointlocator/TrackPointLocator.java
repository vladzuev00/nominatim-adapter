package by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.pointlocator;

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
        return anyCityContain(point) || (point.getSpeed() <= citySpeedThreshold && isUnknownLocation(point));
    }

    private boolean anyCityContain(final TrackPoint point) {
        return geometryService.isAnyContain(cityMap.getCityGeometries(), point);
    }

    private boolean isUnknownLocation(final TrackPoint point) {
        return false;
//        return geometryService
    }
}
