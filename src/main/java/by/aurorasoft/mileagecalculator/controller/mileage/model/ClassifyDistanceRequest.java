package by.aurorasoft.mileagecalculator.controller.mileage.model;

import by.aurorasoft.mileagecalculator.validation.annotation.Latitude;
import by.aurorasoft.mileagecalculator.validation.annotation.Longitude;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Value
public class ClassifyDistanceRequest {

    @NotNull
    @Size(min = 2)
    List<@Valid PointRequest> trackPoints;

    @NotNull
    @PositiveOrZero
    Integer urbanSpeedThreshold;

    @Builder
    @JsonCreator
    public ClassifyDistanceRequest(@JsonProperty("trackPoints") final List<PointRequest> trackPoints,
                                   @JsonProperty("urbanSpeedThreshold") final Integer urbanSpeedThreshold) {
        this.trackPoints = trackPoints;
        this.urbanSpeedThreshold = urbanSpeedThreshold;
    }

    @Value
    public static class PointRequest {

        @Latitude
        Double latitude;

        @Longitude
        Double longitude;

        @NotNull
        @PositiveOrZero
        Integer speed;

        @NotNull
        DistanceRequest gpsDistance;

        @NotNull
        DistanceRequest odometerDistance;

        @Builder
        @JsonCreator
        public PointRequest(@JsonProperty("latitude") final Double latitude,
                            @JsonProperty("longitude") final Double longitude,
                            @JsonProperty("speed") final Integer speed,
                            @JsonProperty("gpsDistance") final DistanceRequest gpsDistance,
                            @JsonProperty("odometerDistance") final DistanceRequest odometerDistance) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.speed = speed;
            this.gpsDistance = gpsDistance;
            this.odometerDistance = odometerDistance;
        }
    }

    @Value
    public static class DistanceRequest {

        @NotNull
        @PositiveOrZero
        Double relative;

        @NotNull
        @PositiveOrZero
        Double absolute;

        @Builder
        @JsonCreator
        public DistanceRequest(@JsonProperty("relative") final Double relative,
                               @JsonProperty("absolute") final Double absolute) {
            this.relative = relative;
            this.absolute = absolute;
        }
    }
}
