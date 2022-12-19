package by.aurorasoft.nominatim.crud.model.dto;

import lombok.Value;

@Value
public class AreaCoordinate {
    Coordinate leftBottom;
    Coordinate rightUpper;
}
