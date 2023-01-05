package by.aurorasoft.nominatim.rest.model;

import by.aurorasoft.nominatim.crud.model.entity.CityEntity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.wololo.geojson.Geometry;

@Value
@Builder
@AllArgsConstructor
public class CityResponse {
    Long id;
    String name;
    Geometry geometry;
    Type type;
}