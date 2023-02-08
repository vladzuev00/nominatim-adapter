package by.aurorasoft.nominatim.service.factory;

import by.aurorasoft.nominatim.service.MileageService;
import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class GeometryByLatLngAltFactory {
    private final GeometryFactory geometryFactory;

    public LineString createLineString(List<? extends LatLngAlt> latLngAlts) {
        final CoordinateSequence coordinateSequence = new CoordinateArraySequence(mapToCoordinates(latLngAlts));
        return new LineString(coordinateSequence, this.geometryFactory);
    }

    public Point createPoint(LatLngAlt latLngAlt) {
        final Coordinate coordinate = mapToCoordinate(latLngAlt);
        return this.geometryFactory.createPoint(coordinate);
    }

    private static CoordinateXY[] mapToCoordinates(List<? extends LatLngAlt> points) {
        return points.stream()
                .map(GeometryByLatLngAltFactory::mapToCoordinate)
                .toArray(CoordinateXY[]::new);
    }

    private static CoordinateXY mapToCoordinate(LatLngAlt mapped) {
        return new CoordinateXY(mapped.getLatitude(), mapped.getLongitude());
    }
}
