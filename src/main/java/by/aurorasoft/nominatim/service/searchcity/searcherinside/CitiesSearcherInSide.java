package by.aurorasoft.nominatim.service.searchcity.searcherinside;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;

public abstract class CitiesSearcherInSide {

    private static final String URI_WITHOUT_PARAMETERS = "https://nominatim.openstreetmap.org/reverse";
    private static final String PARAM_NAME_FORMAT = "format";
    private static final String PARAM_NAME_LATITUDE = "lat";
    private static final String PARAM_NAME_LONGITUDE = "lon";
    private static final String PARAM_NAME_ZOOM = "zoom";

    private static final String FORMAT = "geojson";
    private static final int ZOOM = 10;

    private static final ParameterizedTypeReference<City> PARAMETERIZED_TYPE_REFERENCE
            = new ParameterizedTypeReference<>() {
    };

    private final RestTemplate restTemplate;

    public CitiesSearcherInSide(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public final Collection<City> findCities(AreaCoordinate areaCoordinate, double searchStep) {
        return this.createStreamIteratingBySide(areaCoordinate, searchStep)
                .map(this::findCityByCoordinate)
                .collect(toList());
    }

    protected abstract Stream<Coordinate> createStreamIteratingBySide(AreaCoordinate areaCoordinate,
                                                                      double searchStep);

    private City findCityByCoordinate(Coordinate coordinate) {
        final String uri = findUriToFindCityByCoordinate(coordinate);
        final ResponseEntity<City> responseEntity = this.restTemplate
                .exchange(uri, GET, EMPTY, PARAMETERIZED_TYPE_REFERENCE);
        return responseEntity.getBody();
    }

    private static String findUriToFindCityByCoordinate(Coordinate coordinate) {
        return UriComponentsBuilder
                .fromUriString(URI_WITHOUT_PARAMETERS)
                .queryParam(PARAM_NAME_FORMAT, FORMAT)
                .queryParam(PARAM_NAME_LATITUDE, coordinate.getLatitude())
                .queryParam(PARAM_NAME_LONGITUDE, coordinate.getLongitude())
                .queryParam(PARAM_NAME_ZOOM, ZOOM)
                .build()
                .toUriString();
    }
}
