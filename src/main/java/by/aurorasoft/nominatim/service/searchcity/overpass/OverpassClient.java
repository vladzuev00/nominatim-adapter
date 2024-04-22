package by.aurorasoft.nominatim.service.searchcity.overpass;

import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.model.OverpassSearchCityQuery;
import by.aurorasoft.nominatim.model.OverpassSearchCityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public final class OverpassClient {
    static final String URL = "https://overpass-api.de/api/interpreter";

    private final OverpassSearchCityQueryFactory queryFactory;
    private final RestTemplate restTemplate;

    public OverpassSearchCityResponse findCities(final AreaCoordinate areaCoordinate) {
        final OverpassSearchCityQuery query = queryFactory.create(areaCoordinate);
        final HttpEntity<String> httpEntity = new HttpEntity<>(query.asText());
        return restTemplate.postForObject(URL, httpEntity, OverpassSearchCityResponse.class);
    }
}
