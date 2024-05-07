package by.aurorasoft.distanceclassifier.service.geometry;

import by.aurorasoft.distanceclassifier.model.*;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Bounds;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Relation;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityResponse.Way;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.*;
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
        return createRectangle(
                bounds.getMinLatitude(),
                bounds.getMinLongitude(),
                bounds.getMaxLatitude(),
                bounds.getMaxLongitude()
        );
    }

    public Polygon createPolygon(final AreaCoordinate areaCoordinate) {
        final var min = areaCoordinate.getMin();
        final var max = areaCoordinate.getMax();
        return createRectangle(min.getLatitude(), min.getLongitude(), max.getLatitude(), max.getLongitude());
    }

    public boolean isAnyContain(final Set<PreparedCityGeometry> cityGeometries, final TrackPoint point) {
        return isAnyGeometryContain(cityGeometries, PreparedCityGeometry::getGeometry, point);
    }

    public boolean isAnyBoundingBoxContain(final Set<PreparedCityGeometry> cityGeometries, final TrackPoint point) {
        return isAnyGeometryContain(cityGeometries, PreparedCityGeometry::getBoundingBox, point);
    }

    //TODO: test
    public Polygon createEmptyPolygon() {
        return geometryFactory.createPolygon();
    }

    private CoordinateXY[] getJtsCoordinates(final Track track) {
        return mapToJtsCoordinates(
                track.getPoints(),
                point -> point.getCoordinate().getLatitude(),
                point -> point.getCoordinate().getLongitude()
        );
    }

    private CoordinateXY[] getJtsCoordinates(final Way way) {
        return mapToJtsCoordinates(
                way.getCoordinates(),
                OverpassSearchCityResponse.Coordinate::getLatitude,
                OverpassSearchCityResponse.Coordinate::getLongitude
        );
    }

    private <T> CoordinateXY[] mapToJtsCoordinates(final List<T> sources,
                                                   final ToDoubleFunction<T> latitudeGetter,
                                                   final ToDoubleFunction<T> longitudeGetter) {
        return sources.stream()
                .map(source -> new CoordinateXY(longitudeGetter.applyAsDouble(source), latitudeGetter.applyAsDouble(source)))
                .toArray(CoordinateXY[]::new);
    }

    private Polygon createRectangle(final double minLatitude,
                                    final double minLongitude,
                                    final double maxLatitude,
                                    final double maxLongitude) {
        final CoordinateXY leftBottomJtsCoordinate = new CoordinateXY(minLongitude, minLatitude);
        final CoordinateXY leftUpperJtsCoordinate = new CoordinateXY(minLongitude, maxLatitude);
        final CoordinateXY rightUpperJtsCoordinate = new CoordinateXY(maxLongitude, maxLatitude);
        final CoordinateXY rightBottomJtsCoordinate = new CoordinateXY(maxLongitude, minLatitude);
        return geometryFactory.createPolygon(
                new CoordinateXY[]{
                        leftBottomJtsCoordinate,
                        leftUpperJtsCoordinate,
                        rightUpperJtsCoordinate,
                        rightBottomJtsCoordinate,
                        leftBottomJtsCoordinate
                }
        );
    }

    @SuppressWarnings("unchecked")
    private Polygon[] getJtsPolygons(final Relation relation) {
        final Polygonizer polygonizer = new Polygonizer();
        relation.getMembers()
                .stream()
                .filter(Way.class::isInstance)
                .map(Way.class::cast)
                .map(this::getJtsCoordinates)
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
