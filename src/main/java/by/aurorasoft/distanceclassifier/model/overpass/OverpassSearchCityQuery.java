package by.aurorasoft.distanceclassifier.model.overpass;

import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.model.Coordinate;
import lombok.Value;

@Value
public class OverpassSearchCityQuery {
    int timeout;
    AreaCoordinate areaCoordinate;

    public String asText() {
        final Coordinate min = areaCoordinate.getMin();
        final Coordinate max = areaCoordinate.getMax();
        return """
                [out:json][timeout:%d];
                (
                  relation[place~"(city)|(town)"](%s, %s, %s, %s);
                );
                out geom;"""
                .formatted(timeout, min.getLatitude(), min.getLongitude(), max.getLatitude(), max.getLongitude());
    }
}
