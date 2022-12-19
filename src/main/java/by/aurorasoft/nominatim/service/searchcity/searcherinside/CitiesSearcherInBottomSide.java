package by.aurorasoft.nominatim.service.searchcity.searcherinside;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

import static java.util.stream.DoubleStream.iterate;

@Component
public final class CitiesSearcherInBottomSide extends CitiesSearcherInSide {

    public CitiesSearcherInBottomSide(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected Stream<Coordinate> createStreamIteratingBySide(AreaCoordinate areaCoordinate, double searchStep) {
        final double leftBottomLatitude = areaCoordinate.getLeftBottom().getLatitude();
        final double leftBottomLongitude = areaCoordinate.getLeftBottom().getLongitude();
        return iterate(
                areaCoordinate.getRightUpper().getLatitude(),
                currentLatitude -> Double.compare(currentLatitude, leftBottomLatitude) > 0,
                currentLatitude -> currentLatitude - searchStep)
                .mapToObj(currentLatitude -> new Coordinate(currentLatitude, leftBottomLongitude));
    }
}
