package by.aurorasoft.nominatim.rest.mapper;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.dto.NominatimBbox;
import by.aurorasoft.nominatim.crud.model.dto.NominatimReverseResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class NominatimReverseResponseToCityMapper {
    private final GeometryFactory geometryFactory;

    public City map(NominatimReverseResponse source) {
        return City.builder()
                .name(mapName(source))
                .geometry(mapGeometry(source))
                .build();
    }

    private static String mapName(NominatimReverseResponse source) {
        return source.getNominatimFeatures()
                .get(0)
                .getProperties()
                .getAddress()
                .findName();
    }

    private Geometry mapGeometry(NominatimReverseResponse source) {
        final NominatimBbox bbox = source.getNominatimFeatures().get(0).getBbox();
        final Coordinate leftBottomCoordinate = new Coordinate(bbox.getLeftBottomLatitude(),
                bbox.getLeftBottomLongitude());
        final Coordinate leftUpperCoordinate = new Coordinate(bbox.getLeftBottomLatitude(),
                bbox.getRightUpperLongitude());
        final Coordinate rightUpperCoordinate = new Coordinate(bbox.getRightUpperLatitude(),
                bbox.getRightUpperLongitude());
        final Coordinate rightBottomCoordinate = new Coordinate(bbox.getRightUpperLatitude(),
                bbox.getLeftBottomLongitude());
        return this.geometryFactory.createPolygon(new Coordinate[]{
                leftBottomCoordinate,
                leftUpperCoordinate,
                rightUpperCoordinate,
                rightBottomCoordinate,
                leftBottomCoordinate
        });
    }
}
