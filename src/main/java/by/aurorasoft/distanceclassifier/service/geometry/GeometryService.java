package by.aurorasoft.distanceclassifier.service.geometry;

import by.aurorasoft.distanceclassifier.model.PreparedBoundedGeometry;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Bounds;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Relation;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Way;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.operation.polygonize.Polygonizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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

    //TODO: test
    public boolean isAnyContain(final Set<PreparedBoundedGeometry> geometries, final TrackPoint point) {
        return geometries.stream().anyMatch(geometry -> isContain(geometry, point));
    }

    public boolean isAnyBoundingBoxContain(final Set<PreparedBoundedGeometry> geometries, final TrackPoint point) {
        return geometries.stream()
                .map(PreparedBoundedGeometry::getBoundingBox)
                .anyMatch(geometry -> geometry.contains(geometryFactory.createPoint(createJtsCoordinate(point))));
    }

    private boolean isContain(final PreparedBoundedGeometry geometry, final TrackPoint point) {
        final Coordinate coordinate = createJtsCoordinate(point);
        final Point jtsPoint = geometryFactory.createPoint(coordinate);
        return geometry.getGeometry().contains(jtsPoint);
    }

    //TODO: remove
    public boolean isAnyContain(final List<PreparedGeometry> geometries, final TrackPoint point) {
//        return geometries.stream().anyMatch(geometry -> isContain(geometry, point));
        return false;
    }

    public boolean isContain(final PreparedGeometry geometry, final TrackPoint point) {
        final Coordinate coordinate = createJtsCoordinate(point);
        final Point jtsPoint = geometryFactory.createPoint(coordinate);
        return geometry.contains(jtsPoint);
    }

    public MultiPolygon createMultiPolygon(final Relation relation) {
        return geometryFactory.createMultiPolygon(getPolygons(relation));
    }

    public Polygon createPolygon(final Bounds bounds) {
        return geometryFactory.createPolygon(getJtsCoordinates(bounds));
    }

    @SuppressWarnings("unchecked")
    private Polygon[] getPolygons(final Relation relation) {
        final Polygonizer polygonizer = new Polygonizer();
        relation.getMembers()
                .stream()
                .filter(Way.class::isInstance)
                .map(Way.class::cast)
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

    private static CoordinateXY createJtsCoordinate(final TrackPoint trackPoint) {
        return createJtsCoordinate(
                trackPoint,
                point -> point.getCoordinate().getLatitude(),
                point -> point.getCoordinate().getLongitude()
        );
    }

    private static CoordinateXY createJtsCoordinate(final OverpassSearchCityResponse.Coordinate coordinate) {
        return createJtsCoordinate(
                coordinate,
                OverpassSearchCityResponse.Coordinate::getLatitude,
                OverpassSearchCityResponse.Coordinate::getLongitude
        );
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
