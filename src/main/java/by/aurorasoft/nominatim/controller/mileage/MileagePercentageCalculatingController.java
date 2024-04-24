package by.aurorasoft.nominatim.controller.mileage;

import by.aurorasoft.nominatim.controller.mileage.factory.DistanceCalculatorSettingsFactory;
import by.aurorasoft.nominatim.controller.mileage.factory.TrackFactory;
import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest;
import by.aurorasoft.nominatim.model.MileagePercentage;
import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.service.mileage.MileagePercentageCalculatingService;
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
        final MileagePercentage mileage = calculatingService.calculate(track, distanceCalculatorSettings);
        return ok(mileage);
    }
}
