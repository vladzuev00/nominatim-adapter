package by.aurorasoft.distanceclassifier.service.cityscan.locationappender;

import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ScannedLocationAppender {
    private final GeometryService geometryService;

    public void append(final AreaCoordinate areaCoordinate) {

    }
}
