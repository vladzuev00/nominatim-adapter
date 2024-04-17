package by.aurorasoft.nominatim.controller.city.model;

import by.aurorasoft.nominatim.model.CityType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.wololo.geojson.Geometry;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
@Builder
public class CityRequest {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")
    String name;

    @NotNull
    Geometry geometry;

    @NotNull
    CityType type;

    @JsonCreator
    public CityRequest(@JsonProperty("name") final String name,
                       @JsonProperty("geometry") final Geometry geometry,
                       @JsonProperty("type") final CityType type) {
        this.name = name;
        this.geometry = geometry;
        this.type = type;
    }
}
