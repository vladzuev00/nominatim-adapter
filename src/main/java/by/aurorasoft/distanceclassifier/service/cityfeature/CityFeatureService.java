package by.aurorasoft.distanceclassifier.service.cityfeature;

import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.service.cityfeature.factory.CityFeatureFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CityFeatureService {
    private final CityFeatureFactory featureFactory;
    private final CityService cityService;

    @Transactional(readOnly = true)
    public FeatureCollection getAll() {
        final Feature[] features = getAllFeatures();
        return new FeatureCollection(features);
    }

    private Feature[] getAllFeatures() {
        try (final Stream<City> cities = cityService.streamAll()) {
            return cities.map(featureFactory::create).toArray(Feature[]::new);
        }
    }
}
