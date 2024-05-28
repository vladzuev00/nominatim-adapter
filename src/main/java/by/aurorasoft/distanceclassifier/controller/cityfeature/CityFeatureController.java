package by.aurorasoft.distanceclassifier.controller.cityfeature;

import by.aurorasoft.distanceclassifier.service.cityfeature.CityFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wololo.geojson.FeatureCollection;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cityFeature")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CityFeatureController {
    private final CityFeatureService service;

    @GetMapping
    public ResponseEntity<FeatureCollection> getAll() {
        final FeatureCollection featureCollection = service.getAll();
        return ok(featureCollection);
    }
}
