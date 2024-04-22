package by.aurorasoft.nominatim.service.geometry;

import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Bounds;
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
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

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

    public Polygon createPolygon(final Bounds bounds) {
        return geometryFactory.createPolygon(getJtsCoordinates(bounds));
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

    private static CoordinateXY[] getJtsCoordinates(final Bounds bounds) {
        final CoordinateXY leftBottomCoordinate = getLeftBottomJtsCoordinate(bounds);
        return new CoordinateXY[]{
                leftBottomCoordinate,
                getLeftUpperJtsCoordinate(bounds),
                getRightUpperJtsCoordinate(bounds),
                getRightBottomJtsCoordinate(bounds),
                leftBottomCoordinate
        };
    }

    private static <T> CoordinateXY[] createJtsCoordinates(final List<T> sources,
                                                           final Function<T, CoordinateXY> factory) {
        return createJtsCoordinates(sources.stream(), factory);
    }

    private static <T> CoordinateXY[] createJtsCoordinates(final Stream<T> sources,
                                                           final Function<T, CoordinateXY> factory) {
        return sources.map(factory).toArray(CoordinateXY[]::new);
    }

    private static CoordinateXY createJtsCoordinate(final OverpassTurboSearchCityResponse.Coordinate coordinate) {
        return createJtsCoordinate(
                coordinate,
                OverpassTurboSearchCityResponse.Coordinate::getLatitude,
                OverpassTurboSearchCityResponse.Coordinate::getLongitude
        );
    }

    private static CoordinateXY createJtsCoordinate(final LatLngAlt point) {
        return createJtsCoordinate(point, LatLngAlt::getLatitude, LatLngAlt::getLongitude);
    }

    private static CoordinateXY getLeftBottomJtsCoordinate(final Bounds bounds) {
        return createJtsCoordinate(bounds, Bounds::getMinLatitude, Bounds::getMinLongitude);
    }

    private static CoordinateXY getLeftUpperJtsCoordinate(final Bounds bounds) {
        return createJtsCoordinate(bounds, Bounds::getMaxLatitude, Bounds::getMinLongitude);
    }

    private static CoordinateXY getRightUpperJtsCoordinate(final Bounds bounds) {
        return createJtsCoordinate(bounds, Bounds::getMaxLatitude, Bounds::getMaxLongitude);
    }

    private static CoordinateXY getRightBottomJtsCoordinate(final Bounds bounds) {
        return createJtsCoordinate(bounds, Bounds::getMinLatitude, Bounds::getMaxLongitude);
    }

    private static <T> CoordinateXY createJtsCoordinate(final T source,
                                                        final ToDoubleFunction<T> latitudeGetter,
                                                        final ToDoubleFunction<T> longitudeGetter) {
        final double latitude = latitudeGetter.applyAsDouble(source);
        final double longitude = longitudeGetter.applyAsDouble(source);
        return new CoordinateXY(longitude, latitude);
    }
}
