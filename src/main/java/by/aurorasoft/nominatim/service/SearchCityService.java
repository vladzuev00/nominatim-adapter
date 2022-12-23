package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.crud.model.dto.NominatimReverseResponse;
import by.aurorasoft.nominatim.crud.model.dto.NominatimReverseResponse.ExtraTags;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.client.NominatimService;
import by.aurorasoft.nominatim.rest.mapper.NominatimReverseResponseToCityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static by.aurorasoft.nominatim.util.StreamUtil.asStream;
import static java.lang.Double.compare;
import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public final class SearchCityService {
    private static final String TEMPLATE_EXCEPTION_DESCRIPTION_NOT_VALID_AREA_COORDINATE
            = "Area's coordinate '%s' isn't valid.";
    private static final String REGEX_PLACE_VALUE_IN_JSON_OF_CITY = "(city)|(town)";

    private final NominatimService nominatimService;
    private final NominatimReverseResponseToCityMapper mapper;
    private final CityService cityService;

    public void findInAreaAndSave(AreaCoordinate areaCoordinate, double searchStep) {
        if (!isValidAreaCoordinate(areaCoordinate)) {
            throw new IllegalArgumentException(
                    format(TEMPLATE_EXCEPTION_DESCRIPTION_NOT_VALID_AREA_COORDINATE, areaCoordinate));
        }
        final Set<String> namesAlreadyFoundCities = new HashSet<>();
        final Set<City> foundCities = asStream(new AreaIterator(areaCoordinate, searchStep))
                .map(this.nominatimService::reverse)
                .filter(SearchCityService::isCity)
                .map(this.mapper::map)
                .filter(city -> namesAlreadyFoundCities.add(city.getName()))
                .collect(toSet());
        this.cityService.saveAll(foundCities);
    }

    private static boolean isValidAreaCoordinate(AreaCoordinate areaCoordinate) {
        final Coordinate leftBottom = areaCoordinate.getLeftBottom();
        final Coordinate rightUpper = areaCoordinate.getRightUpper();
        return compare(leftBottom.getLatitude(), rightUpper.getLatitude()) < 0
                && compare(leftBottom.getLongitude(), rightUpper.getLongitude()) < 0;
    }

    private static boolean isCity(NominatimReverseResponse response) {
        final ExtraTags extraTags = response.getExtraTags();
        return extraTags != null
                && extraTags.getPlace() != null
                && extraTags.getPlace().matches(REGEX_PLACE_VALUE_IN_JSON_OF_CITY);
    }

    private static final class AreaIterator implements Iterator<Coordinate> {
        private final AreaCoordinate areaCoordinate;
        private final double searchStep;
        private Coordinate current;

        public AreaIterator(AreaCoordinate areaCoordinate, double searchStep) {
            this.areaCoordinate = areaCoordinate;
            this.searchStep = searchStep;
            this.current = new Coordinate(
                    areaCoordinate.getLeftBottom().getLatitude() - searchStep,
                    areaCoordinate.getLeftBottom().getLongitude()
            );
        }

        @Override
        public boolean hasNext() {
            return this.hasNextLatitude() || this.hasNextLongitude();
        }

        @Override
        public Coordinate next() {
            if (this.hasNextLatitude()) {
                return this.nextLatitude();
            } else if (this.hasNextLongitude()) {
                return this.nextLongitude();
            }
            throw new NoSuchElementException();
        }

        private boolean hasNextLatitude() {
            return compare(
                    this.current.getLatitude() + this.searchStep,
                    this.areaCoordinate.getRightUpper().getLatitude()) <= 0;
        }

        private boolean hasNextLongitude() {
            return compare(
                    this.current.getLongitude() + this.searchStep,
                    this.areaCoordinate.getRightUpper().getLongitude()) <= 0;
        }

        private Coordinate nextLatitude() {
            this.current = new Coordinate(
                    this.current.getLatitude() + this.searchStep,
                    this.current.getLongitude());
            return this.current;
        }

        private Coordinate nextLongitude() {
            this.current = new Coordinate(
                    this.areaCoordinate.getLeftBottom().getLatitude(),
                    this.current.getLongitude() + this.searchStep);
            return this.current;
        }
    }
}
