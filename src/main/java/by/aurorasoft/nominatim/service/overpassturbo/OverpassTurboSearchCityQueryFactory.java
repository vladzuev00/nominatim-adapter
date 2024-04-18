package by.aurorasoft.nominatim.service.overpassturbo;

import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.model.OverpassTurboSearchCityQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class OverpassTurboSearchCityQueryFactory {
    private final int timeout;

    public OverpassTurboSearchCityQueryFactory(@Value("${overpass-turbo.timeout}") final int timeout) {
        this.timeout = timeout;
    }

    public OverpassTurboSearchCityQuery create(final AreaCoordinate areaCoordinate) {
        return new OverpassTurboSearchCityQuery(timeout, areaCoordinate);
    }
}
