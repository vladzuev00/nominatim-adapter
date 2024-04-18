package by.aurorasoft.nominatim.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

@Value
public class OverpassTurboSearchCityQuery {
    int timeout;
    AreaCoordinate areaCoordinate;

    @JsonValue
    public String asText() {
        return """
                [out:json][timeout:%d];
                (
                  node["place"~"(city)|(town)"](%f, %f, %f, %f);
                );
                out geom;"""
                .formatted(
                        timeout,
                        areaCoordinate.getLeftBottom().getLatitude(),
                        areaCoordinate.getLeftBottom().getLongitude(),
                        areaCoordinate.getRightUpper().getLatitude(),
                        areaCoordinate.getRightUpper().getLongitude()
                );
    }
}
