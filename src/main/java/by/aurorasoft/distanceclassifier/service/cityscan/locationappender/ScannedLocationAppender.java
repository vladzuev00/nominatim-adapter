package by.aurorasoft.distanceclassifier.service.cityscan.locationappender;

import by.aurorasoft.distanceclassifier.crud.service.ScannedLocationService;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ScannedLocationAppender {
    private final GeometryService geometryService;
    private final ScannedLocationService locationService;

    public void append(final AreaCoordinate areaCoordinate) {
        final Polygon polygon = geometryService.createPolygon(areaCoordinate);
        locationService.append(polygon);
    }
}
