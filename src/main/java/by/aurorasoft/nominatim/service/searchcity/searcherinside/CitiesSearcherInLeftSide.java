package by.aurorasoft.nominatim.service.searchcity.searcherinside;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

import static java.util.stream.DoubleStream.iterate;

@Component
public final class CitiesSearcherInLeftSide extends CitiesSearcherInSide {

    public CitiesSearcherInLeftSide(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected Stream<Coordinate> createStreamIteratingBySide(AreaCoordinate areaCoordinate, double searchStep) {
        final double leftBottomLatitude = areaCoordinate.getLeftBottom().getLatitude();
        final double leftUpperLongitude = areaCoordinate.getRightUpper().getLongitude();
        return iterate(
                areaCoordinate.getLeftBottom().getLongitude(),
                currentLongitude -> Double.compare(currentLongitude, leftUpperLongitude) < 0,
                currentLongitude -> currentLongitude + searchStep)
                .mapToObj(currentLongitude -> new Coordinate(leftBottomLatitude, currentLongitude));
    }
}
