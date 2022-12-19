package by.aurorasoft.nominatim.service.searchcity.searcherinside;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

import static java.util.stream.DoubleStream.iterate;

@Component
public final class CitiesSearcherInUpperSide extends CitiesSearcherInSide {

    public CitiesSearcherInUpperSide(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected Stream<Coordinate> createStreamIteratingBySide(AreaCoordinate areaCoordinate, double searchStep) {
        final double rightUpperLatitude = areaCoordinate.getRightUpper().getLatitude();
        final double rightUpperLongitude = areaCoordinate.getRightUpper().getLongitude();
        return iterate(
                areaCoordinate.getLeftBottom().getLatitude(),
                currentLatitude -> Double.compare(currentLatitude, rightUpperLatitude) < 0,
                currentLatitude -> currentLatitude + searchStep)
                .mapToObj(currentLatitude -> new Coordinate(currentLatitude, rightUpperLongitude));
    }
}
