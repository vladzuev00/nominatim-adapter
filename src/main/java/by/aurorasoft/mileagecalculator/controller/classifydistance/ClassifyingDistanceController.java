package by.aurorasoft.mileagecalculator.controller.classifydistance;

import by.aurorasoft.mileagecalculator.controller.classifydistance.factory.TrackFactory;
import by.aurorasoft.mileagecalculator.controller.classifydistance.model.ClassifyDistanceRequest;
import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.service.mileage.ClassifyingDistanceService;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequestMapping("/api/v1/classifyDistance")
@RequiredArgsConstructor
public class ClassifyingDistanceController {
    private final TrackFactory trackFactory;
    private final ClassifyingDistanceService service;

    @PostMapping
    public ResponseEntity<ClassifiedDistanceStorage> classify(@Valid @RequestBody final ClassifyDistanceRequest request) {
        final Track track = trackFactory.create(request);
        final ClassifiedDistanceStorage storage = service.classify(track, request.getUrbanSpeedThreshold());
        return ok(storage);
    }
}
