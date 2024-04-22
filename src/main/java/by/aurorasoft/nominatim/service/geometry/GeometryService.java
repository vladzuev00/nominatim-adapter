package by.aurorasoft.nominatim.service.geometry;

import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Relation;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Way;
import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.operation.polygonize.Polygonizer;
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

    public Geometry createMultiPolygon(final Relation relation) {
        final List<LineString> lines = relation.getWays()
                .stream()
                .map(this::createLine)
                .toList();
        final Polygonizer polygonizer = new Polygonizer();
        polygonizer.add(lines);
        final Polygon[] polygons = (Polygon[]) polygonizer.getPolygons().toArray(Polygon[]::new);
        return geometryFactory.createMultiPolygon(polygons);
    }

    private LineString createLine(final Way way) {
        return geometryFactory.createLineString(mapToCoordinates(way));
    }

    private static CoordinateSequence mapToCoordinateSequence(final Track track) {
        final CoordinateXY[] coordinates = mapToCoordinates(track.getPoints());
        return new CoordinateArraySequence(coordinates);
    }

    private static CoordinateXY[] mapToCoordinates(final Way way) {
        return way.getCoordinates()
                .stream()
                .map(c -> new CoordinateXY(c.getLongitude(), c.getLatitude()))
                .toArray(CoordinateXY[]::new);
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
