package by.aurorasoft.nominatim.model;

import lombok.Value;

@Value
public class AreaCoordinate {
    Coordinate min;
    Coordinate max;
}
