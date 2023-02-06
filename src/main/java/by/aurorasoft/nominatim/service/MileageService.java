package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.MileageResponse;
import by.nhorushko.distancecalculator.*;
import by.nhorushko.trackfilter.TrackFilter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class MileageService {
    private static final double EPSILON_TO_FILTER_TRACK_POINTS = 0.00015;

    private final TrackFilter trackFilter;
    private final DistanceCalculator distanceCalculator;
    private final CityService cityService;
    private final GeometryFactory geometryFactory;

    public MileageResponse findMileage(MileageRequest request) {
        final DistanceCalculatorSettings distanceCalculatorSettings = new DistanceCalculatorSettingsImpl(
                request.getMinDetectionSpeed(), request.getMaxMessageTimeout());
        final List<? extends LatLngAlt> significantTrackPoints = this.trackFilter.filter(request.getTrackPoints(),
                EPSILON_TO_FILTER_TRACK_POINTS);
        final DistanceCalculator calculator = new DistanceCalculatorImpl();
        final LineString lineString = new LineString(
                new CoordinateArraySequence(significantTrackPoints.stream()
                        .map(significantTrackPoint -> new CoordinateXY(significantTrackPoint.getLatitude(), significantTrackPoint.getLongitude()))
                        .toArray(CoordinateXY[]::new)
                        ),
                this.geometryFactory);
        return null;
    }
}
