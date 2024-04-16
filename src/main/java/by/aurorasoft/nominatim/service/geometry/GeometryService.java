package by.aurorasoft.nominatim.service.geometry;

import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class GeometryService {
    private final GeometryFactory geometryFactory;

    public LineString createLine(final Track track) {
        final CoordinateSequence coordinateSequence = mapToCoordinateSequence(track);
        return new LineString(coordinateSequence, geometryFactory);
    }

    public boolean isAnyContain(final List<PreparedGeometry> geometries, final TrackPoint point) {
        return geometries.stream().anyMatch(geometry -> isContain(geometry, point));
    }

    private static CoordinateSequence mapToCoordinateSequence(final Track track) {
        final CoordinateXY[] coordinates = mapToCoordinates(track.getPoints());
        return new CoordinateArraySequence(coordinates);
    }

    private static CoordinateXY[] mapToCoordinates(final List<? extends LatLngAlt> points) {
        return points.stream()
                .map(GeometryService::mapToCoordinate)
                .toArray(CoordinateXY[]::new);
    }

    private static CoordinateXY mapToCoordinate(final LatLngAlt point) {
        return new CoordinateXY(point.getLongitude(), point.getLatitude());
    }

    private boolean isContain(final PreparedGeometry geometry, final LatLngAlt latLngAlt) {
        final Point point = mapToPoint(latLngAlt);
        return geometry.contains(point);
    }

    private Point mapToPoint(final LatLngAlt latLngAlt) {
        final Coordinate coordinate = mapToCoordinate(latLngAlt);
        return geometryFactory.createPoint(coordinate);
    }
}
