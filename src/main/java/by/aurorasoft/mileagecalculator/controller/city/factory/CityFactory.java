package by.aurorasoft.mileagecalculator.controller.city.factory;

import by.aurorasoft.mileagecalculator.controller.city.model.CityRequest;
import by.aurorasoft.mileagecalculator.crud.model.dto.City;
import by.aurorasoft.mileagecalculator.crud.model.dto.City.CityBuilder;
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
        final Geometry geometry = getGeometry(request);
        return City.builder()
                .name(request.getName())
                .geometry(getGeometry(request))
                .type(request.getType())
                .boundingBox(geometry.getEnvelope());
    }

    private Geometry getGeometry(final CityRequest request) {
        return geoJSONReader.read(request.getGeometry());
    }
}
