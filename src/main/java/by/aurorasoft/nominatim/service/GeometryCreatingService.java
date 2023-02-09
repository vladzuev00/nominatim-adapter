package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class GeometryCreatingService {
    private final GeometryFactory geometryFactory;

    public LineString createLineString(List<? extends LatLngAlt> latLngAlts) {
        final CoordinateSequence coordinateSequence = new CoordinateArraySequence(mapToCoordinates(latLngAlts));
        return new LineString(coordinateSequence, this.geometryFactory);
    }

    public Point createPoint(LatLngAlt latLngAlt) {
        final Coordinate coordinate = mapToCoordinate(latLngAlt);
        return this.geometryFactory.createPoint(coordinate);
    }

    public GeometryCollection createByCities(List<City> cities) {
        final Geometry[] geometries = cities.stream()
                .map(City::getGeometry)
                .toArray(Geometry[]::new);
        return new GeometryCollection(geometries, this.geometryFactory);
    }

    private static CoordinateXY[] mapToCoordinates(List<? extends LatLngAlt> points) {
        return points.stream()
                .map(GeometryCreatingService::mapToCoordinate)
                .toArray(CoordinateXY[]::new);
    }

    private static CoordinateXY mapToCoordinate(LatLngAlt mapped) {
        return new CoordinateXY(mapped.getLongitude(), mapped.getLatitude());
    }
}
