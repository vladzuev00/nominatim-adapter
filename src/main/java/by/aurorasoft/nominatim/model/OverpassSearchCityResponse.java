package by.aurorasoft.nominatim.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Value;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Value
public class OverpassSearchCityResponse {
    List<Relation> relations;

    @JsonCreator
    public OverpassSearchCityResponse(@JsonProperty(value = "elements", required = true) final List<Relation> relations) {
        this.relations = relations;
    }

    @Value
    public static class Relation {
        Bounds bounds;
        List<Member> members;
        Tags tags;

        @Builder
        @JsonCreator
        public Relation(@JsonProperty(value = "bounds", required = true) final Bounds bounds,
                        @JsonProperty(value = "members", required = true) final List<Member> members,
                        @JsonProperty(value = "tags", required = true) final Tags tags) {
            this.bounds = bounds;
            this.members = members;
            this.tags = tags;
        }
    }

    @Value
    public static class Bounds {
        double minLatitude;
        double minLongitude;
        double maxLatitude;
        double maxLongitude;

        @Builder
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

        @JsonCreator
        public Coordinate(@JsonProperty(value = "lat", required = true) final double latitude,
                          @JsonProperty(value = "lon", required = true) final double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @JsonTypeInfo(use = NAME, property = "type")
    @JsonSubTypes({@Type(value = Node.class, name = "node"), @Type(value = Way.class, name = "way")})
    public interface Member {

    }

    @Value
    public static class Node implements Member {
        Coordinate coordinate;

        @JsonCreator
        public Node(@JsonProperty(value = "lat", required = true) final double latitude,
                    @JsonProperty(value = "lon", required = true) final double longitude) {
            coordinate = new Coordinate(latitude, longitude);
        }
    }

    @Value
    public static class Way implements Member {
        List<Coordinate> coordinates;

        @JsonCreator
        public Way(@JsonProperty(value = "geometry", required = true) final List<Coordinate> coordinates) {
            this.coordinates = coordinates;
        }
    }

    @Value
    public static class Tags {
        String capital;
        String name;
        String place;

        @Builder
        @JsonCreator
        public Tags(@JsonProperty("capital") final String capital,
                    @JsonProperty(value = "name", required = true) final String name,
                    @JsonProperty(value = "place", required = true) final String place) {
            this.capital = capital;
            this.name = name;
            this.place = place;
        }
    }
}
