package by.aurorasoft.nominatim.rest.model;

import by.nhorushko.distancecalculator.LatLngAlt;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.List;

@Value
@Builder
public class MileageRequest {

    @NotNull
    List<TrackPoint> trackPoints;

    @NotNull
    @Min(0)
    @Max(1000)
    Integer minDetectionSpeed;

    @NotNull
    @Min(0)
    @Max(1000)
    Integer maxMessageTimeout;

    @JsonCreator
    public MileageRequest(@JsonProperty("trackPoints") List<TrackPoint> trackPoints,
                          @JsonProperty("minDetectionSpeed") Integer minDetectionSpeed,
                          @JsonProperty("maxMessageTimeout") Integer maxMessageTimeout) {
        this.trackPoints = trackPoints;
        this.minDetectionSpeed = minDetectionSpeed;
        this.maxMessageTimeout = maxMessageTimeout;
    }

    @Value
    @Builder
    public static class TrackPoint implements LatLngAlt {

        @NotNull
        @PastOrPresent
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
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
        public TrackPoint(@JsonProperty("datetime") Instant datetime,
                          @JsonProperty("latitude") Float latitude,
                          @JsonProperty("longitude") Float longitude,
                          @JsonProperty("altitude") Integer altitude,
                          @JsonProperty("speed") Integer speed,
                          @JsonProperty("valid") Boolean valid) {
            this.datetime = datetime;
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
            this.speed = speed;
            this.valid = valid;
        }

        @Override
        public float getLatitude() {
            return this.latitude;
        }

        @Override
        public float getLongitude() {
            return this.longitude;
        }

        @Override
        public int getAltitude() {
            return this.altitude;
        }

        @Override
        public int getSpeed() {
            return this.speed;
        }

        @Override
        public boolean isValid() {
            return this.valid;
        }
    }
}
