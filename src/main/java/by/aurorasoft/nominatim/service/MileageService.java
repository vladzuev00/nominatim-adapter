package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.MileageResponse;
import by.nhorushko.distancecalculator.*;
import by.nhorushko.trackfilter.TrackFilter;
import lombok.Value;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.rangeClosed;

@Service
public final class MileageService {
    private static final double EPSILON_TO_FILTER_TRACK_POINTS = 0.00015;

    private final TrackFilter trackFilter;
    private final DistanceCalculator distanceCalculator;
    private final CityService cityService;
    private final GeometryCreatingService geometryCreatingService;
    private final AtomicReference<Map<PreparedGeometry, PreparedGeometry>> cityGeometriesByBoundingBoxAtomicReference;

    public MileageService(TrackFilter trackFilter, DistanceCalculator distanceCalculator,
                          CityService cityService, GeometryCreatingService geometryCreatingService) {
        this.trackFilter = trackFilter;
        this.distanceCalculator = distanceCalculator;
        this.cityService = cityService;
        this.geometryCreatingService = geometryCreatingService;
        this.cityGeometriesByBoundingBoxAtomicReference = new AtomicReference<>();
    }

    public MileageResponse findMileage(MileageRequest request) {
        this.cityGeometriesByBoundingBoxAtomicReference.compareAndSet(
                null, this.cityService.findPreparedGeometriesByPreparedBoundingBoxes());
        final DistanceCalculatorSettings distanceCalculatorSettings = new DistanceCalculatorSettingsImpl(
                request.getMinDetectionSpeed(), request.getMaxMessageTimeout());
        final List<TrackSlice> trackSlices = this.findTrackSlices(request.getTrackPoints());
        final Map<Boolean, Double> mileagesByLocatedInCity = this.findMileagesByLocatedInCity(trackSlices,
                distanceCalculatorSettings);
        return new MileageResponse(
                mileagesByLocatedInCity.get(true),
                mileagesByLocatedInCity.get(false)
        );
    }

    private Map<Boolean, Double> findMileagesByLocatedInCity(List<TrackSlice> trackSlices,
                                                             DistanceCalculatorSettings distanceCalculatorSettings) {
        return trackSlices.stream()
                .collect(
                        partitioningBy(
                                TrackSlice::isLocatedInCity,
                                summingDouble(slice -> this.calculateDistance(slice, distanceCalculatorSettings))
                        )
                );
    }

    private double calculateDistance(TrackSlice trackSlice, DistanceCalculatorSettings distanceCalculatorSettings) {
        return this.distanceCalculator.calculateDistance(trackSlice.firstPoint, trackSlice.secondPoint,
                distanceCalculatorSettings);
    }

    private List<TrackSlice> findTrackSlices(List<? extends LatLngAlt> trackPoints) {
        final List<PreparedGeometry> intersectedCitiesGeometries
                = this.findGeometriesIntersectedByLineStringOfPoints(trackPoints);
        final int indexPenultimatePoint = trackPoints.size() - 2;
        return rangeClosed(0, indexPenultimatePoint)
                .mapToObj(i -> new TrackSlice(
                        trackPoints.get(i),
                        trackPoints.get(i + 1),
                        //slices, which is located in city, must have second point, which is located in city
                        this.isAnyGeometryContainPoint(trackPoints.get(i + 1), intersectedCitiesGeometries)
                ))
                .collect(toList());
    }

    private List<PreparedGeometry> findGeometriesIntersectedByLineStringOfPoints(
            List<? extends LatLngAlt> trackPoints) {
        final List<? extends LatLngAlt> significantTrackPointsToCreateLineString = this.trackFilter.filter(
                trackPoints, EPSILON_TO_FILTER_TRACK_POINTS);
        final LineString lineString = this.geometryCreatingService.createLineString(
                significantTrackPointsToCreateLineString);
        return this.cityGeometriesByBoundingBoxAtomicReference.get()
                .entrySet()
                .stream()
                .filter(geometryByBoundingBox -> geometryByBoundingBox.getKey().intersects(lineString))
                .map(Map.Entry::getValue)
                .collect(toList());
    }

    private boolean isAnyGeometryContainPoint(LatLngAlt latLngAlt, List<PreparedGeometry> geometries) {
        final Point point = this.geometryCreatingService.createPoint(latLngAlt);
        return geometries
                .stream()
                .anyMatch(geometry -> geometry.contains(point));
    }

    @Value
    private static class TrackSlice {
        LatLngAlt firstPoint;
        LatLngAlt secondPoint;
        boolean locatedInCity;
    }
}
