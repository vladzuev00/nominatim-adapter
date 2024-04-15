package by.aurorasoft.nominatim.crud.model.dto;

import by.aurorasoft.nominatim.model.CityType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.locationtech.jts.geom.Geometry;

@Value
@AllArgsConstructor
@Builder
public class City implements AbstractDto<Long> {
    Long id;
    String name;
    Geometry geometry;
    CityType type;
    Geometry boundingBox;
}
