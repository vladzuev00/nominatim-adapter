package by.aurorasoft.distanceclassifier.controller.city.factory;

import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse;
import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse.CityGeometryResponse;
import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

@Component
@RequiredArgsConstructor
public final class CityResponseFactory {
    private final GeoJSONWriter geoJSONWriter;

    public CityResponse create(final City city) {
        return new CityResponse(city.getId(), city.getName(), city.getType(), getGeometry(city));
    }

    private CityGeometryResponse getGeometry(final City city) {
        final CityGeometry source = city.getGeometry();
        final Geometry geometry = geoJSONWriter.write(source.getGeometry());
        final Geometry boundingBox = geoJSONWriter.write(source.getBoundingBox());
        return new CityGeometryResponse(geometry, boundingBox);
    }
}
