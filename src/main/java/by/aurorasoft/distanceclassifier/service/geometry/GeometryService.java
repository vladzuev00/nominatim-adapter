package by.aurorasoft.distanceclassifier.service.geometry;

import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Bounds;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Relation;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Way;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
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

    public MultiPolygon createMultiPolygon(final Relation relation) {
        return geometryFactory.createMultiPolygon(getJtsPolygons(relation));
    }

    public Polygon createPolygon(final Bounds bounds) {
        return geometryFactory.createPolygon(getJtsCoordinates(bounds));
    }

    public boolean isAnyContain(final Set<PreparedCityGeometry> cityGeometries, final TrackPoint point) {
        return isAnyPropertyContain(cityGeometries, point, PreparedCityGeometry::getGeometry);
    }

    public boolean isAnyBoundingBoxContain(final Set<PreparedCityGeometry> cityGeometries, final TrackPoint point) {
        return isAnyPropertyContain(cityGeometries, point, PreparedCityGeometry::getBoundingBox);
    }

    private static CoordinateXY[] getJtsCoordinates(final Way way) {
        return createJtsCoordinates(way.getCoordinates(), OverpassSearchCityResponse.Coordinate::getLatitude, OverpassSearchCityResponse.Coordinate::getLongitude);
    }

    private static CoordinateXY[] getJtsCoordinates(final Track track) {
        return createJtsCoordinates(track.getPoints(), point -> point.getCoordinate().getLatitude(), point -> point.getCoordinate().getLongitude());
    }

    private static CoordinateXY[] getJtsCoordinates(final Bounds bounds) {
        final CoordinateXY leftBottomJtsCoordinate = getLeftBottomJtsCoordinate(bounds);
        return new CoordinateXY[]{
                leftBottomJtsCoordinate,
                getLeftUpperJtsCoordinate(bounds),
                getRightUpperJtsCoordinate(bounds),
                getRightBottomJtsCoordinate(bounds),
                leftBottomJtsCoordinate
        };
    }

    private static <T> CoordinateXY[] createJtsCoordinates(final List<T> sources,
                                                           final ToDoubleFunction<T> latitudeGetter,
                                                           final ToDoubleFunction<T> longitudeGetter) {
        return sources.stream()
                .map(source -> createJtsCoordinate(source, latitudeGetter, longitudeGetter))
                .toArray(CoordinateXY[]::new);
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

    @SuppressWarnings("unchecked")
    private Polygon[] getJtsPolygons(final Relation relation) {
        final Polygonizer polygonizer = new Polygonizer();
        relation.getMembers()
                .stream()
                .filter(Way.class::isInstance)
                .map(Way.class::cast)
                .map(this::createJtsLine)
                .forEach(polygonizer::add);
        return (Polygon[]) polygonizer.getPolygons().toArray(Polygon[]::new);
    }

    private LineString createJtsLine(final Way way) {
        return geometryFactory.createLineString(getJtsCoordinates(way));
    }

    private boolean isAnyPropertyContain(final Set<PreparedCityGeometry> cityGeometries,
                                         final TrackPoint point,
                                         final Function<PreparedCityGeometry, PreparedGeometry> propertyGetter) {
        return cityGeometries.stream()
                .map(propertyGetter)
                .anyMatch(geometry -> isContain(geometry, point));
    }

    private boolean isContain(final PreparedGeometry geometry, final TrackPoint point) {
        final Coordinate jtsCoordinate = createJtsCoordinate(
                point,
                source -> source.getCoordinate().getLatitude(),
                source -> source.getCoordinate().getLongitude()
        );
        final Point jtsPoint = geometryFactory.createPoint(jtsCoordinate);
        return geometry.contains(jtsPoint);
    }
}
