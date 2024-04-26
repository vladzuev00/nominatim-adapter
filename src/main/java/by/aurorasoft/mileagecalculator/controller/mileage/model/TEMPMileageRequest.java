package by.aurorasoft.mileagecalculator.controller.mileage.model;

import by.aurorasoft.mileagecalculator.validation.annotation.Latitude;
import by.aurorasoft.mileagecalculator.validation.annotation.Longitude;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.Instant;
import java.util.List;

//TODO: remove
@Value
@Builder
public class TEMPMileageRequest {

    @NotNull
    @Size(min = 2)
    List<@Valid TEMPTrackPointRequest> trackPoints;

    @NotNull
    @Min(0)
    Integer minDetectionSpeed;

    @NotNull
    @Min(0)
    Integer maxMessageTimeout;

    @JsonCreator
    public TEMPMileageRequest(@JsonProperty("trackPoints") final List<TEMPTrackPointRequest> trackPoints,
                              @JsonProperty("minDetectionSpeed") final Integer minDetectionSpeed,
                              @JsonProperty("maxMessageTimeout") final Integer maxMessageTimeout) {
        this.trackPoints = trackPoints;
        this.minDetectionSpeed = minDetectionSpeed;
        this.maxMessageTimeout = maxMessageTimeout;
    }

    @Value
    @Builder
    public static class TEMPTrackPointRequest {

        @NotNull
        @PastOrPresent
        Instant datetime;

        @Latitude
        Double latitude;

        @Longitude
        Double longitude;

        @NotNull
        Integer altitude;

        @NotNull
        @Min(0)
        @Max(1000)
        Integer speed;

        @NotNull
        Boolean valid;

        @JsonCreator
        public TEMPTrackPointRequest(@JsonProperty("datetime") final Instant datetime,
                                     @JsonProperty("latitude") final Double latitude,
                                     @JsonProperty("longitude") final Double longitude,
                                     @JsonProperty("altitude") final Integer altitude,
                                     @JsonProperty("speed") final Integer speed,
                                     @JsonProperty("valid") final Boolean valid) {
            this.datetime = datetime;
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
            this.speed = speed;
            this.valid = valid;
        }
    }
}
