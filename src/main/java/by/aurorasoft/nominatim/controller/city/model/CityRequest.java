package by.aurorasoft.nominatim.controller.city.model;

import by.aurorasoft.nominatim.model.CityType;
import by.aurorasoft.nominatim.validation.annotation.CityName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.wololo.geojson.Geometry;

import javax.validation.constraints.NotNull;

@Value
public class CityRequest {

    @CityName
    String name;

    @NotNull
    Geometry geometry;

    @NotNull
    CityType type;

    @Builder
    @JsonCreator
    public CityRequest(@JsonProperty("name") final String name,
                       @JsonProperty("geometry") final Geometry geometry,
                       @JsonProperty("type") final CityType type) {
        this.name = name;
        this.geometry = geometry;
        this.type = type;
    }
}
