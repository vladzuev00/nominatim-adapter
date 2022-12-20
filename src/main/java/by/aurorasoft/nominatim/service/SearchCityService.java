package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Double.compare;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.iterate;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;

@Service
@RequiredArgsConstructor
public class SearchCityService {
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

    public Collection<City> findCitiesInArea(AreaCoordinate areaCoordinate, double searchStep) {
        final Set<String> namesAlreadyFoundCities = new HashSet<>();
        return iterate(areaCoordinate.getLeftBottom(),
                currentCoordinate -> hasNextCoordinate(currentCoordinate, areaCoordinate, searchStep),
                currentCoordinate -> findNextCoordinate(currentCoordinate, areaCoordinate, searchStep))
                .map(this::findCityByCoordinate)
                .filter(city -> namesAlreadyFoundCities.add(city.getName()))
                .collect(toSet());
    }

    private static boolean hasNextCoordinate(Coordinate currentCoordinate, AreaCoordinate areaCoordinate,
                                             double searchStep) {
        return compare(currentCoordinate.getLatitude() + searchStep, areaCoordinate.getRightUpper().getLatitude()) <= 0
                || compare(currentCoordinate.getLongitude() + searchStep, areaCoordinate.getRightUpper().getLongitude()) <= 0;
    }

    private static Coordinate findNextCoordinate(Coordinate currentCoordinate, AreaCoordinate areaCoordinate,
                                                 double searchStep) {
        if (compare(currentCoordinate.getLatitude() + searchStep, areaCoordinate.getRightUpper().getLatitude()) > 0) {
            return new Coordinate(areaCoordinate.getLeftBottom().getLatitude(),
                    currentCoordinate.getLongitude() + searchStep);
        } else {
            return new Coordinate(currentCoordinate.getLatitude() + searchStep,
                    currentCoordinate.getLongitude());
        }
    }

    private City findCityByCoordinate(Coordinate coordinate) {
        final String uri = findUriToFindCityByCoordinate(coordinate);
        //TODO: refactor
        System.out.println(uri);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    private static final class Area implements Iterable<Coordinate> {
        private final AreaCoordinate coordinate;
        private final double searchStep;

        @Override
        public Iterator<Coordinate> iterator() {
            return null;
        }

        private static final class AreaIterator implements Iterator<Coordinate> {
            private Coordinate currentCoordinate;

            public

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Coordinate next() {
                return null;
            }
        }
    }
}
