package by.aurorasoft.mileagecalculator.controller.mileage;

import by.aurorasoft.mileagecalculator.controller.mileage.factory.DistanceCalculatorSettingsFactory;
import by.aurorasoft.mileagecalculator.controller.mileage.factory.TrackFactory;
import by.aurorasoft.mileagecalculator.controller.mileage.model.MileageRequest;
import by.aurorasoft.mileagecalculator.model.MileagePercentage;
import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.service.mileage.MileagePercentageCalculatingService;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
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
    private final DistanceCalculatorSettingsFactory distanceCalculatorSettingsFactory;
    private final MileagePercentageCalculatingService calculatingService;

    @PostMapping
    public ResponseEntity<MileagePercentage> calculate(@Valid @RequestBody final MileageRequest request) {
        final Track track = trackFactory.create(request);
        final DistanceCalculatorSettings distanceCalculatorSettings = distanceCalculatorSettingsFactory.create(request);
        final MileagePercentage mileagePercentage = calculatingService.calculate(track, distanceCalculatorSettings);
        return ok(mileagePercentage);
    }
}
