package by.aurorasoft.nominatim.controller.cityscan.model;

import by.aurorasoft.nominatim.validation.annotation.Latitude;
import by.aurorasoft.nominatim.validation.annotation.Longitude;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class AreaCoordinateRequest {

    @Latitude
    Double minLatitude;

    @Longitude
    Double minLongitude;

    @Latitude
    Double maxLatitude;

    @Longitude
    Double maxLongitude;
}
