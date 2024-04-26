package by.aurorasoft.mileagecalculator.controller.city.factory;

import by.aurorasoft.mileagecalculator.controller.city.model.CityResponse;
import by.aurorasoft.mileagecalculator.crud.model.dto.City;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

@Component
@RequiredArgsConstructor
public final class CityResponseFactory {
    private final GeoJSONWriter geoJSONWriter;

    public CityResponse create(final City city) {
        return new CityResponse(city.getId(), city.getName(), getGeometry(city), city.getType());
    }

    private Geometry getGeometry(final City city) {
        return geoJSONWriter.write(city.getGeometry());
    }
}