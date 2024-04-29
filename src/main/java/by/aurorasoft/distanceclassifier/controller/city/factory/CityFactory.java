package by.aurorasoft.distanceclassifier.controller.city.factory;

import by.aurorasoft.distanceclassifier.controller.city.model.CityRequest;
import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityBuilder;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONReader;

@Component
@RequiredArgsConstructor
public final class CityFactory {
    private final GeoJSONReader geoJSONReader;

    public City create(final CityRequest request) {
        return createBuilder(request).build();
    }

    public City create(final Long id, final CityRequest request) {
        return createBuilder(request)
                .id(id)
                .build();
    }

    private CityBuilder createBuilder(final CityRequest request) {
        return City.builder()
                .name(request.getName())
                .type(request.getType())
                .geometry(getGeometry(request));
    }

    private CityGeometry getGeometry(final CityRequest request) {
        final Geometry geometry = geoJSONReader.read(request.getGeometry());
        final Geometry boundingBox = geometry.getEnvelope();
        return new CityGeometry(geometry, boundingBox);
    }
}
