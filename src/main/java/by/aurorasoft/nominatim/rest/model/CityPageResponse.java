package by.aurorasoft.nominatim.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class CityPageResponse {
    int pageNumber;
    int pageSize;
    List<CityResponse> cities;
}
