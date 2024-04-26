package by.aurorasoft.mileagecalculator.controller.mileage;

import by.aurorasoft.mileagecalculator.controller.mileage.factory.TrackFactory;
import by.aurorasoft.mileagecalculator.controller.mileage.model.ClassifyDistanceRequest;
import by.aurorasoft.mileagecalculator.model.MileagePercentage;
import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.service.mileage.MileagePercentageCalculatingService;
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
@RequestMapping("/api/v1/mileagePercentage")
@RequiredArgsConstructor
public class MileagePercentageCalculatingController {
    private final TrackFactory trackFactory;
    private final MileagePercentageCalculatingService service;

    @PostMapping
    public ResponseEntity<MileagePercentage> calculate(@Valid @RequestBody final ClassifyDistanceRequest request) {
        final Track track = trackFactory.create(request);
//        final MileagePercentage percentage = service.calculate(track, request.getUrbanSpeedThreshold());
        return ok(null);
    }
}
