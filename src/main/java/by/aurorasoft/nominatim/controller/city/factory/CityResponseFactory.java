package by.aurorasoft.nominatim.controller.city.factory;

import by.aurorasoft.nominatim.controller.city.model.CityResponse;
import by.aurorasoft.nominatim.crud.model.dto.City;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

@Component
@RequiredArgsConstructor
public final class CityResponseFactory {
    private final GeoJSONWriter geoJSONWriter;

    public CityResponse create(final City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .geometry(getGeometry(city))
                .type(city.getType())
                .build();
    }

    private Geometry getGeometry(final City city) {
        return geoJSONWriter.write(city.getGeometry());
    }
}
