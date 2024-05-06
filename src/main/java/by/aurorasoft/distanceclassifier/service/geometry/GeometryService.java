package by.aurorasoft.distanceclassifier.service.geometry;

import by.aurorasoft.distanceclassifier.model.*;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Bounds;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Relation;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Way;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.operation.polygonize.Polygonizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

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

    //TODO:
    public Polygon createPolygon(final AreaCoordinate areaCoordinate) {
        return null;
    }

    public boolean isAnyContain(final Set<PreparedCityGeometry> cityGeometries, final TrackPoint point) {
        return isAnyGeometryContain(cityGeometries, PreparedCityGeometry::getGeometry, point);
    }

    public boolean isAnyBoundingBoxContain(final Set<PreparedCityGeometry> cityGeometries, final TrackPoint point) {
        return isAnyGeometryContain(cityGeometries, PreparedCityGeometry::getBoundingBox, point);
    }

    private static CoordinateXY[] getJtsCoordinates(final Track track) {
        return mapToJtsCoordinates(
                track.getPoints(),
                point -> point.getCoordinate().getLatitude(),
                point -> point.getCoordinate().getLongitude()
        );
    }

    private static CoordinateXY[] getJtsCoordinates(final Way way) {
        return mapToJtsCoordinates(
                way.getCoordinates(),
                OverpassSearchCityResponse.Coordinate::getLatitude,
                OverpassSearchCityResponse.Coordinate::getLongitude
        );
    }

    private static <T> CoordinateXY[] mapToJtsCoordinates(final List<T> sources,
                                                          final ToDoubleFunction<T> latitudeGetter,
                                                          final ToDoubleFunction<T> longitudeGetter) {
        return sources.stream()
                .map(source -> new CoordinateXY(longitudeGetter.applyAsDouble(source), latitudeGetter.applyAsDouble(source)))
                .toArray(CoordinateXY[]::new);
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

    private static CoordinateXY getLeftBottomJtsCoordinate(final Bounds bounds) {
        return new CoordinateXY(bounds.getMinLongitude(), bounds.getMinLatitude());
    }

    private static CoordinateXY getLeftUpperJtsCoordinate(final Bounds bounds) {
        return new CoordinateXY(bounds.getMinLongitude(), bounds.getMaxLatitude());
    }

    private static CoordinateXY getRightUpperJtsCoordinate(final Bounds bounds) {
        return new CoordinateXY(bounds.getMaxLongitude(), bounds.getMaxLatitude());
    }

    private static CoordinateXY getRightBottomJtsCoordinate(final Bounds bounds) {
        return new CoordinateXY(bounds.getMaxLongitude(), bounds.getMinLatitude());
    }

    @SuppressWarnings("unchecked")
    private Polygon[] getJtsPolygons(final Relation relation) {
        final Polygonizer polygonizer = new Polygonizer();
        relation.getMembers()
                .stream()
                .filter(Way.class::isInstance)
                .map(Way.class::cast)
                .map(GeometryService::getJtsCoordinates)
                .map(geometryFactory::createLineString)
                .forEach(polygonizer::add);
        return (Polygon[]) polygonizer.getPolygons().toArray(Polygon[]::new);
    }

    private boolean isAnyGeometryContain(final Set<PreparedCityGeometry> cityGeometries,
                                         final Function<PreparedCityGeometry, PreparedGeometry> geometryGetter,
                                         final TrackPoint point) {
        final Coordinate jtsCoordinate = new CoordinateXY(point.getLongitude(), point.getLatitude());
        final Point jtsPoint = geometryFactory.createPoint(jtsCoordinate);
        return cityGeometries.stream()
                .map(geometryGetter)
                .anyMatch(geometry -> geometry.contains(jtsPoint));
    }
}
