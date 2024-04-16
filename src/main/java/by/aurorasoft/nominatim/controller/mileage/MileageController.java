package by.aurorasoft.nominatim.controller.mileage;

import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import by.aurorasoft.nominatim.model.Mileage;
import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.MileageRequest.RequestTrackPoint;
import by.aurorasoft.nominatim.service.mileage.MileageCalculatingService;
import by.nhorushko.distancecalculator.DistanceCalculatorSettingsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/mileage")
@Validated
@RequiredArgsConstructor
public class MileageController {
    private final MileageCalculatingService calculatingService;

    @PostMapping
    public ResponseEntity<Mileage> findMileage(@Valid @RequestBody final MileageRequest mileageRequest) {
        final Mileage mileage = calculatingService.calculate(createTrack(mileageRequest), new DistanceCalculatorSettingsImpl(mileageRequest.getMinDetectionSpeed(), mileageRequest.getMaxMessageTimeout()));
        return ok(mileage);
    }

    private static Track createTrack(final MileageRequest request) {
        final List<TrackPoint> trackPoints = request.getTrackPoints().stream().map(MileageController::mapToTrackPoint).toList();
        return new Track(trackPoints);
    }

    private static TrackPoint mapToTrackPoint(final RequestTrackPoint request) {
        return new TrackPoint(
                request.getDatetime(),
                request.getLatitude(),
                request.getLongitude(),
                request.getAltitude(),
                request.getSpeed(),
                request.isValid()
        );
    }
}
