package by.aurorasoft.nominatim.model;

import lombok.Value;

@Value
public class OverpassTurboSearchCityQuery {
    int timeout;
    AreaCoordinate areaCoordinate;

    public String asText() {
        return """
                [out:json][timeout:%d];
                (
                  relation[place~"(city)|(town)"](%s, %s, %s, %s);
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
