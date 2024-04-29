package by.aurorasoft.distanceclassifier.controller.city.model;

import by.aurorasoft.distanceclassifier.model.CityType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.wololo.geojson.Geometry;

@Value
public class CityResponse {
    Long id;
    String name;
    CityType type;
    CityGeometryResponse geometry;

    @JsonCreator
    public CityResponse(@JsonProperty("id") final Long id,
                        @JsonProperty("name") final String name,
                        @JsonProperty("type") final CityType type,
                        @JsonProperty("geometry") final CityGeometryResponse geometry) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.geometry = geometry;
    }

    @Value
    public static class CityGeometryResponse {
        Geometry geometry;
        Geometry boundingBox;

        @JsonCreator
        public CityGeometryResponse(@JsonProperty("geometry") final Geometry geometry,
                                    @JsonProperty("boundingBox") final Geometry boundingBox) {
            this.geometry = geometry;
            this.boundingBox = boundingBox;
        }
    }
}
