package by.aurorasoft.nominatim.controller.mileage.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.Instant;
import java.util.List;

@Value
@Builder
public class MileageRequest {

    @NotNull
    @Size(min = 2)
    List<@Valid RequestTrackPoint> trackPoints;

    @NotNull
    @Min(0)
    Integer minDetectionSpeed;

    @NotNull
    @Min(0)
    Integer maxMessageTimeout;

    @JsonCreator
    public MileageRequest(@JsonProperty("trackPoints") List<RequestTrackPoint> trackPoints,
                          @JsonProperty("minDetectionSpeed") Integer minDetectionSpeed,
                          @JsonProperty("maxMessageTimeout") Integer maxMessageTimeout) {
        this.trackPoints = trackPoints;
        this.minDetectionSpeed = minDetectionSpeed;
        this.maxMessageTimeout = maxMessageTimeout;
    }

    @Value
    @Builder
    public static class RequestTrackPoint {

        @NotNull
        @PastOrPresent
        Instant datetime;

        @NotNull
        @DecimalMin("-90")
        @DecimalMax("90")
        Float latitude;

        @NotNull
        @DecimalMin("-180")
        @DecimalMax("180")
        Float longitude;

        @NotNull
        Integer altitude;

        @NotNull
        @Min(0)
        @Max(1000)
        Integer speed;

        @NotNull
        Boolean valid;

        @JsonCreator
        public RequestTrackPoint(@JsonProperty("datetime") final Instant datetime,
                                 @JsonProperty("latitude") final Float latitude,
                                 @JsonProperty("longitude") final Float longitude,
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
