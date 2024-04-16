package by.aurorasoft.nominatim.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Mileage {
    double urban;
    double country;

    @JsonCreator
    public Mileage(@JsonProperty("urban") final double urban, @JsonProperty("country") final double country) {
        this.urban = urban;
        this.country = country;
    }
}
