package by.aurorasoft.distanceclassifier.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.locationtech.jts.geom.Geometry;

@Value
@AllArgsConstructor
@Builder
public class ScannedLocation implements AbstractDto<Long> {
    Long id;
    Geometry geometry;
}
