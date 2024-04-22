package by.aurorasoft.nominatim.service.searchcity.overpass;

import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityQuery;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.TEXT_PLAIN;

@Component
@RequiredArgsConstructor
public final class OverpassTurboClient {
    private static final String URL = "https://overpass-api.de/api/interpreter";

    private final OverpassTurboSearchCityQueryFactory queryFactory;
    private final RestTemplate restTemplate;

    public OverpassTurboSearchCityResponse findCities(final AreaCoordinate areaCoordinate) {
        final OverpassTurboSearchCityQuery query = queryFactory.create(areaCoordinate);
        final HttpEntity<String> httpEntity = createHttpEntity(query);
        return restTemplate.postForObject(URL, httpEntity, OverpassTurboSearchCityResponse.class);
    }

    private static HttpEntity<String> createHttpEntity(final OverpassTurboSearchCityQuery query) {
        final String queryText = query.asText();
        final HttpHeaders headers = createHttpHeaders();
        return new HttpEntity<>(queryText, headers);
    }

    private static HttpHeaders createHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(TEXT_PLAIN);
        return headers;
    }
}
