package by.aurorasoft.distanceclassifier.service.cityscan.overpass;

import by.aurorasoft.distanceclassifier.config.property.OverpassProperty;
import by.aurorasoft.distanceclassifier.model.AreaCoordinate;
import by.aurorasoft.distanceclassifier.model.OverpassSearchCityQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class OverpassSearchCityQueryFactory {
    private final OverpassProperty property;

    public OverpassSearchCityQuery create(final AreaCoordinate areaCoordinate) {
        return new OverpassSearchCityQuery(property.getTimeout(), areaCoordinate);
    }
}
