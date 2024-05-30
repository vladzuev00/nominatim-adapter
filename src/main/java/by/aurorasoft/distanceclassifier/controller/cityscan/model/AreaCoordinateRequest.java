package by.aurorasoft.distanceclassifier.controller.cityscan.model;

import by.aurorasoft.distanceclassifier.validation.annotation.Latitude;
import by.aurorasoft.distanceclassifier.validation.annotation.Longitude;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
public class AreaCoordinateRequest {

    @Latitude
    double minLatitude;

    @Longitude
    double minLongitude;

    @Latitude
    double maxLatitude;

    @Longitude
    double maxLongitude;

    @Builder
    @JsonCreator
    public AreaCoordinateRequest(@JsonProperty("minLatitude") final double minLatitude,
                                 @JsonProperty("minLongitude") final double minLongitude,
                                 @JsonProperty("maxLatitude") final double maxLatitude,
                                 @JsonProperty("maxLongitude") final double maxLongitude) {
        this.minLatitude = minLatitude;
        this.minLongitude = minLongitude;
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
    }
}
