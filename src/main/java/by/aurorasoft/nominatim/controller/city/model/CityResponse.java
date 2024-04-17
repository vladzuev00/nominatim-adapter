package by.aurorasoft.nominatim.controller.city.model;

import by.aurorasoft.nominatim.model.CityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.wololo.geojson.Geometry;

/**
 * equals and hashcode doesn't work correctly because of geometry doesn't override them
 */
@Value
@Builder
@AllArgsConstructor
public class CityResponse {
    Long id;
    String name;
    Geometry geometry;
    CityType type;
}
