package by.aurorasoft.distanceclassifier.service.cityfeature;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wololo.geojson.FeatureCollection;

@Service
@RequiredArgsConstructor
public final class CityFeatureService {
    private final CityService cityService;

    public FeatureCollection getAll() {
        final FeatureCollection collection = new FeatureCollection(null);
        cityService.streamAll();
        return null;
    }
}
