package by.aurorasoft.nominatim.crud.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.ARRAY;

@Value
@JsonFormat(shape = ARRAY)
//TODO: remove next line if possible
//@JsonPropertyOrder({"leftBottomLatitude", "leftBottomLongitude", "rightUpperLatitude", "rightUpperLongitude"})
public class NominatimBbox {
    double leftBottomLatitude;
    double leftBottomLongitude;
    double rightUpperLatitude;
    double rightUpperLongitude;

    @JsonCreator
    public NominatimBbox(@JsonProperty("leftBottomLatitude") double leftBottomLatitude,
                         @JsonProperty("leftBottomLongitude") double leftBottomLongitude,
                         @JsonProperty("rightUpperLatitude") double rightUpperLatitude,
                         @JsonProperty("rightUpperLongitude") double rightUpperLongitude) {
        this.leftBottomLatitude = leftBottomLatitude;
        this.leftBottomLongitude = leftBottomLongitude;
        this.rightUpperLatitude = rightUpperLatitude;
        this.rightUpperLongitude = rightUpperLongitude;
    }
}
