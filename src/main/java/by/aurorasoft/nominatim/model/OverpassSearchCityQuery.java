package by.aurorasoft.nominatim.model;

import lombok.Value;

@Value
public class OverpassSearchCityQuery {
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
                        areaCoordinate.getMin().getLatitude(),
                        areaCoordinate.getMin().getLongitude(),
                        areaCoordinate.getMax().getLatitude(),
                        areaCoordinate.getMax().getLongitude()
                );
    }
}
