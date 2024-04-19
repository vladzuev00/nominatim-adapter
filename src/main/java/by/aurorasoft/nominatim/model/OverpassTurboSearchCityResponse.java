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
        List<Way> ways;
        Tags tags;

        @JsonCreator
        public Relation(@JsonProperty(value = "bounds", required = true) final Bounds bounds,
                        @JsonProperty(value = "members", required = true) final List<Way> ways,
                        @JsonProperty(value = "tags", required = true) final Tags tags) {
            this.bounds = bounds;
            this.ways = ways;
            this.tags = tags;
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
    public static class Way {
        List<Coordinate> coordinates;

        @JsonCreator
        public Way(@JsonProperty(value = "geometry", required = true) final List<Coordinate> coordinates) {
            this.coordinates = coordinates;
        }
    }

    @Value
    public static class Coordinate {
        double latitude;
        double longitude;

        @JsonCreator
        public Coordinate(@JsonProperty(value = "lat", required = true) final double latitude,
                          @JsonProperty(value = "lon", required = true) final double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Value
    public static class Tags {
        String capital;
        String name;

        @JsonCreator
        public Tags(@JsonProperty("capital") final String capital,
                    @JsonProperty(value = "name:en", required = true) final String name) {
            this.capital = capital;
            this.name = name;
        }
    }
}
