package by.aurorasoft.nominatim.service.geometry;

import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Relation;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Way;
import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.operation.polygonize.Polygonizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public final class GeometryService {
    private final GeometryFactory geometryFactory;

    public LineString createLine(final Track track) {
        return geometryFactory.createLineString(getJtsCoordinates(track));
    }

    public boolean isAnyContain(final List<PreparedGeometry> geometries, final TrackPoint point) {
        return geometries.stream().anyMatch(geometry -> isContain(geometry, point));
    }

    public MultiPolygon createMultiPolygon(final Relation relation) {
        return geometryFactory.createMultiPolygon(getPolygons(relation));
    }

    private boolean isContain(final PreparedGeometry geometry, final LatLngAlt latLngAlt) {
        final Coordinate coordinate = createJtsCoordinate(latLngAlt);
        final Point point = geometryFactory.createPoint(coordinate);
        return geometry.contains(point);
    }

    @SuppressWarnings("unchecked")
    private Polygon[] getPolygons(final Relation relation) {
        final Polygonizer polygonizer = new Polygonizer();
        relation.getWays()
                .stream()
                .map(this::createLine)
                .forEach(polygonizer::add);
        return (Polygon[]) polygonizer.getPolygons().toArray(Polygon[]::new);
    }

    private LineString createLine(final Way way) {
        return geometryFactory.createLineString(getJtsCoordinates(way));
    }

    private static CoordinateXY[] getJtsCoordinates(final Way way) {
        return createJtsCoordinates(way.getCoordinates(), GeometryService::createJtsCoordinate);
    }

    private static CoordinateXY[] getJtsCoordinates(final Track track) {
        return createJtsCoordinates(track.getPoints(), GeometryService::createJtsCoordinate);
    }

    private static <T> CoordinateXY[] createJtsCoordinates(final List<T> sources,
                                                           final Function<T, CoordinateXY> factory) {
        return sources.stream()
                .map(factory)
                .toArray(CoordinateXY[]::new);
    }

    private static CoordinateXY createJtsCoordinate(final OverpassTurboSearchCityResponse.Coordinate coordinate) {
        return new CoordinateXY(coordinate.getLongitude(), coordinate.getLatitude());
    }

    private static CoordinateXY createJtsCoordinate(final LatLngAlt point) {
        return new CoordinateXY(point.getLongitude(), point.getLatitude());
    }
}
