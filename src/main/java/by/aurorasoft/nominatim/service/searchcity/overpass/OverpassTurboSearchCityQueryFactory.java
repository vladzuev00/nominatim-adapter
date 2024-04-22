package by.aurorasoft.nominatim.service.searchcity.overpass;

import by.aurorasoft.nominatim.model.AreaCoordinate;
import by.aurorasoft.nominatim.model.OverpassSearchCityQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class OverpassTurboSearchCityQueryFactory {
    private final int timeout;

    public OverpassTurboSearchCityQueryFactory(@Value("${overpass-turbo.timeout}") final int timeout) {
        this.timeout = timeout;
    }

    public OverpassSearchCityQuery create(final AreaCoordinate areaCoordinate) {
        return new OverpassSearchCityQuery(timeout, areaCoordinate);
    }
}
