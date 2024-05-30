package by.aurorasoft.distanceclassifier.controller.classifydistance.model;

import by.aurorasoft.distanceclassifier.validation.annotation.Latitude;
import by.aurorasoft.distanceclassifier.validation.annotation.Longitude;
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

    @PositiveOrZero
    int urbanSpeedThreshold;

    @Builder
    @JsonCreator
    public ClassifyDistanceRequest(@JsonProperty("trackPoints") final List<PointRequest> trackPoints,
                                   @JsonProperty("urbanSpeedThreshold") int urbanSpeedThreshold) {
        this.trackPoints = trackPoints;
        this.urbanSpeedThreshold = urbanSpeedThreshold;
    }

    @Value
    public static class PointRequest {

        @Latitude
        double latitude;

        @Longitude
        double longitude;

        @PositiveOrZero
        int speed;

        @NotNull
        DistanceRequest gpsDistance;

        @NotNull
        DistanceRequest odometerDistance;

        @Builder
        @JsonCreator
        public PointRequest(@JsonProperty("latitude") double latitude,
                            @JsonProperty("longitude") double longitude,
                            @JsonProperty("speed") int speed,
                            @JsonProperty("gpsDistance") DistanceRequest gpsDistance,
                            @JsonProperty("odoDistance") DistanceRequest odometerDistance) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.speed = speed;
            this.gpsDistance = gpsDistance;
            this.odometerDistance = odometerDistance;
        }
    }

    @Value
    public static class DistanceRequest {

        @PositiveOrZero
        double relative;

        @PositiveOrZero
        double absolute;

        @Builder
        @JsonCreator
        public DistanceRequest(@JsonProperty("relative") double relative,
                               @JsonProperty("absolute") double absolute) {
            this.relative = relative;
            this.absolute = absolute;
        }
    }
}
