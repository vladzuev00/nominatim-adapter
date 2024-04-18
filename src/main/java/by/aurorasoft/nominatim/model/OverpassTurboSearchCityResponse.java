package by.aurorasoft.nominatim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wololo.geojson.Geometry;

public class OverpassTurboSearchCityResponse {

    public static class City {
        Geometry boundingBox;
        String name;

        public City(@JsonProperty final Geometry boundingBox,
                    @JsonProperty(value = "name:en", required = true) final String name,
                    @JsonProperty(value = "capital", required = true) final String capital) {

        }
    }
}
