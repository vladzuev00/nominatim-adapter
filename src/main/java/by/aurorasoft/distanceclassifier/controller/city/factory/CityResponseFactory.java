package by.aurorasoft.distanceclassifier.controller.city.factory;

import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse;
import by.aurorasoft.distanceclassifier.crud.model.dto.City;
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
        return null;
//        return geoJSONWriter.write(city.getGeometry());
    }
}
