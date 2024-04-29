package by.aurorasoft.distanceclassifier.crud.model.dto;

import by.aurorasoft.distanceclassifier.model.CityType;
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
    CityType type;
    CityGeometry geometry;

    @Value
    public static class CityGeometry {
        Geometry geometry;
        Geometry boundingBox;
    }
}
