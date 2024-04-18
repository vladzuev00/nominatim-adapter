package by.aurorasoft.nominatim.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.wololo.geojson.Geometry;

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
        String name;
        String capital;
        String place;
        Geometry boundingBox;
        Geometry geometry;

        @JsonCreator
        public Relation(@JsonProperty(value = "name:ru", required = true) final String name,
                        @JsonProperty(value = "capital") final String capital,
                        @JsonProperty(value = "place", required = true) final String place,
                        @JsonProperty(value = "bounds") final Geometry boundingBox,
                        @JsonProperty(value = "geometry", required = true) final Geometry geometry) {
            this.name = name;
            this.capital = capital;
            this.place = place;
            this.boundingBox = boundingBox;
            this.geometry = geometry;
        }
    }
}
