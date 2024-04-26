package by.aurorasoft.distanceclassifier.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

//TODO: remove
@Value
public class MileagePercentage {
    double urban;
    double country;

    @JsonCreator
    public MileagePercentage(@JsonProperty("urban") final double urban, @JsonProperty("country") final double country) {
        this.urban = urban;
        this.country = country;
    }
}
