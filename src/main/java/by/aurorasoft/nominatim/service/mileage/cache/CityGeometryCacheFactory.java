package by.aurorasoft.nominatim.service.mileage.cache;

import by.aurorasoft.nominatim.crud.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//TODO: load only if true in application.yml
@Component
@RequiredArgsConstructor
public final class CityGeometryCacheFactory {
    private final CityService cityService;

    public CityGeometryCache create() {
        final var geometriesByBoundingBoxes = cityService.findPreparedGeometriesByPreparedBoundingBoxes();
        return new CityGeometryCache(geometriesByBoundingBoxes);
    }
}
