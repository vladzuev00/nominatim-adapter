package by.aurorasoft.distanceclassifier.controller.classifydistance;

import by.aurorasoft.distanceclassifier.controller.classifydistance.factory.TrackFactory;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.ClassifyingDistanceService;
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
public class ClassifyDistanceController {
    private final TrackFactory trackFactory;
    private final ClassifyingDistanceService service;

    @PostMapping
    public ResponseEntity<ClassifiedDistanceStorage> classify(@Valid @RequestBody final ClassifyDistanceRequest request) {
        final Track track = trackFactory.create(request);
        final ClassifiedDistanceStorage storage = service.classify(track, request.getUrbanSpeedThreshold());
        return ok(storage);
    }
}
