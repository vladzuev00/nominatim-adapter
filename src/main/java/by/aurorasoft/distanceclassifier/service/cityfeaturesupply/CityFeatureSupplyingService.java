package by.aurorasoft.distanceclassifier.service.cityfeaturesupply;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wololo.geojson.FeatureCollection;

@Service
@RequiredArgsConstructor
public final class CityFeatureSupplyingService {
    private final CityService cityService;

    public FeatureCollection get() {
        final FeatureCollection collection = new FeatureCollection(null);
        cityService.streamAll();
        return null;
    }
}
