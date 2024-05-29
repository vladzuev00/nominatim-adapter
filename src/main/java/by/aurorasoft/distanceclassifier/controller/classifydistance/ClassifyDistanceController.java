package by.aurorasoft.distanceclassifier.controller.classifydistance;

import by.aurorasoft.distanceclassifier.controller.classifydistance.factory.TrackFactory;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassify.ClassifyDistanceService;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/classifyDistance")
@RequiredArgsConstructor
public class ClassifyDistanceController {
    private final TrackFactory trackFactory;
    private final ClassifyDistanceService service;
    private final ClassifyDistanceLogAggregator logAggregator;

    @PostMapping
    public ResponseEntity<ClassifiedDistanceStorage> classify(@Valid @RequestBody ClassifyDistanceRequest request) {
        long start = System.currentTimeMillis();
        var track = trackFactory.create(request);
        var classifiedDistanceStorage = service.classify(track, request.getUrbanSpeedThreshold());
        logAggregator.addRequest(classifiedDistanceStorage, System.currentTimeMillis() - start);
        return ok(classifiedDistanceStorage);
    }
}
