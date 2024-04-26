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
    Double minLatitude;

    @Longitude
    Double minLongitude;

    @Latitude
    Double maxLatitude;

    @Longitude
    Double maxLongitude;

    @Builder
    @JsonCreator
    public AreaCoordinateRequest(@JsonProperty("minLatitude") final Double minLatitude,
                                 @JsonProperty("minLongitude") final Double minLongitude,
                                 @JsonProperty("maxLatitude") final Double maxLatitude,
                                 @JsonProperty("maxLongitude") final Double maxLongitude) {
        this.minLatitude = minLatitude;
        this.minLongitude = minLongitude;
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
    }
}
