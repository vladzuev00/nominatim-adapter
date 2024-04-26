package by.aurorasoft.distanceclassifier.controller.city.model;

import by.aurorasoft.distanceclassifier.model.CityType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.wololo.geojson.Geometry;

@Value
public class CityResponse {
    Long id;
    String name;
    Geometry geometry;
    CityType type;

    @Builder
    @JsonCreator
    public CityResponse(@JsonProperty("id") final Long id,
                        @JsonProperty("name") final String name,
                        @JsonProperty("geometry") final Geometry geometry,
                        @JsonProperty("type") final CityType type) {
        this.id = id;
        this.name = name;
        this.geometry = geometry;
        this.type = type;
    }
}
