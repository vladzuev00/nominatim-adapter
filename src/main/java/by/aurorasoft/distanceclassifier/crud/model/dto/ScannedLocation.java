package by.aurorasoft.distanceclassifier.crud.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.locationtech.jts.geom.Geometry;

@Value
@AllArgsConstructor
@Builder
public class ScannedLocation {
    Long id;
    Geometry geometry;
}
