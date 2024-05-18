package by.aurorasoft.distanceclassifier.service.cityfeaturesupply.factory;

import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Feature;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.Map;

@Component
@RequiredArgsConstructor
public final class CityFeatureFactory {
    private static final String PROPERTY_KEY_ID = "id";
    private static final String PROPERTY_KEY_NAME = "name";
    private static final String PROPERTY_KEY_TYPE = "cityType";

    private final GeoJSONWriter geoJSONWriter;

    public Feature create(final City city) {
        final Geometry geometry = createGeometry(city);
        final Map<String, Object> properties = createProperties(city);
        return new Feature(geometry, properties);
    }

    private Geometry createGeometry(final City city) {
        return geoJSONWriter.write(city.getGeometry().getGeometry());
    }

    private Map<String, Object> createProperties(final City city) {
        return Map.of(
                PROPERTY_KEY_ID, city.getId(),
                PROPERTY_KEY_NAME, city.getName(),
                PROPERTY_KEY_TYPE, city.getType()
        );
    }
}
