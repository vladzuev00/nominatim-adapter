package by.aurorasoft.mileagecalculator.service.cityscan.overpass;

import by.aurorasoft.mileagecalculator.model.AreaCoordinate;
import by.aurorasoft.mileagecalculator.model.OverpassSearchCityQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class OverpassSearchCityQueryFactory {
    private final int timeout;

    public OverpassSearchCityQueryFactory(@Value("${overpass.timeout}") final int timeout) {
        this.timeout = timeout;
    }

    public OverpassSearchCityQuery create(final AreaCoordinate areaCoordinate) {
        return new OverpassSearchCityQuery(timeout, areaCoordinate);
    }
}
