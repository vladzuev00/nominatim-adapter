package by.aurorasoft.nominatim.service.searchcity.searcherinside;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

import static java.util.stream.DoubleStream.iterate;

@Component
public final class CitiesSearcherInRightSide extends CitiesSearcherInSide {

    public CitiesSearcherInRightSide(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected Stream<Coordinate> createStreamIteratingBySide(AreaCoordinate areaCoordinate, double searchStep) {
        final double rightUpperLatitude = areaCoordinate.getRightUpper().getLatitude();
        final double leftBottomLongitude = areaCoordinate.getLeftBottom().getLongitude();
        return iterate(
                areaCoordinate.getRightUpper().getLongitude(),
                currentLongitude -> Double.compare(currentLongitude, leftBottomLongitude) > 0,
                currentLongitude -> currentLongitude - searchStep)
                .mapToObj(currentLongitude -> new Coordinate(rightUpperLatitude, currentLongitude));
    }
}
