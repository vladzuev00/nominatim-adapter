package by.aurorasoft.nominatim.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class OverpassTurboSearchCityResponse {
    List<Relation> relations;

    @JsonCreator
    public OverpassTurboSearchCityResponse(@JsonProperty(value = "elements", required = true) final List<Relation> relations) {
        this.relations = relations;
    }

    @Value
    public static class Relation {
        Bounds bounds;

        @JsonCreator
        public Relation(@JsonProperty(value = "bounds", required = true) final Bounds bounds) {
            this.bounds = bounds;
        }
    }

    @Value
    public static class Bounds {
        double minLatitude;
        double minLongitude;
        double maxLatitude;
        double maxLongitude;

        @JsonCreator
        public Bounds(@JsonProperty(value = "minlat", required = true) final double minLatitude,
                      @JsonProperty(value = "minlon", required = true) final double minLongitude,
                      @JsonProperty(value = "maxlat", required = true) final double maxLatitude,
                      @JsonProperty(value = "maxlon", required = true) final double maxLongitude) {
            this.minLatitude = minLatitude;
            this.minLongitude = minLongitude;
            this.maxLatitude = maxLatitude;
            this.maxLongitude = maxLongitude;
        }
    }

    @Value
    public static class Coordinate {
        double latitude;
        double longitude;

        public Coordinate(@JsonProperty(value = "lat", required = true) final double latitude,
                          @JsonProperty(value = "lon", required = true) final double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Value
    public static class Tags {
        String capital;      //capital
        String name;         //name:en
    }
}
