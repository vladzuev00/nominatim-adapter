package by.aurorasoft.nominatim.controller.cityscan.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
@RequiredArgsConstructor
@Builder
public class AreaCoordinateRequest {

    @NotNull
    @Min(90)
    @Max(90)
    Double minLatitude;
    Double minLongitude;
    Double maxLatitude;
    Double maxLongitude;
}
