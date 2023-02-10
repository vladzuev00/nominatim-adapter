package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.City;
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

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.rangeClosed;

@Service
public final class MileageService {
    private static final double EPSILON_TO_FILTER_TRACK_POINTS = 0.00015;
    private static final double DISTANCE_IN_CASE_NO_TRACK_SLICES = 0.;

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
        long beforeWithoutDBQuery = currentTimeMillis();
        try {
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
        finally {
            long afterWithoutDBQuery = currentTimeMillis();
            out.println("Without db query: " + (afterWithoutDBQuery - beforeWithoutDBQuery));
        }
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
        long before = currentTimeMillis();
        try {
            final List<PreparedGeometry> intersectedCitiesGeometries = this.findGeometriesIntersectedByLineStringOfPoints(trackPoints);
            final int indexPenultimatePoint = trackPoints.size() - 2;
            long beforeStreamOperation = currentTimeMillis();
            try {
                return rangeClosed(0, indexPenultimatePoint)
                        .parallel()
                        .mapToObj(i -> new TrackSlice(
                                trackPoints.get(i),
                                trackPoints.get(i + 1),
                                //slices, which is located in city, must have second point, which is located in city
                                this.isAnyGeometryContainPoint(trackPoints.get(i + 1), intersectedCitiesGeometries)
                        ))
                        .collect(toList());
            } finally {
                long afterStreamOperation = currentTimeMillis();
                out.println("Stream operation : " + (afterStreamOperation - beforeStreamOperation));
            }
        } finally {
            long after = currentTimeMillis();
            out.println("Finding track slices: " + MILLISECONDS.toSeconds(after - before));
        }
    }

    private List<PreparedGeometry> findGeometriesIntersectedByLineStringOfPoints(
            List<? extends LatLngAlt> trackPoints) {
        long before = currentTimeMillis();
        try {
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
        } finally {
            long after = currentTimeMillis();
            out.println("Finding cities intersected by line string: " + MILLISECONDS.toSeconds(after - before));
        }
    }

    private boolean isAnyGeometryContainPoint(LatLngAlt latLngAlt, List<PreparedGeometry> geometries) {
        final Point point = this.geometryCreatingService.createPoint(latLngAlt);
        return geometries
                .stream()
                .anyMatch(geometry -> geometry.contains(point));
    }

    private static boolean isPointInsideCity(Point point, City city) {
        return city.getGeometry().contains(point);
    }

    private double calculateDistance(List<TrackSlice> trackSlices,
                                     DistanceCalculatorSettings distanceCalculatorSettings) {
        final long before = currentTimeMillis();
        try {
            return trackSlices.stream()
                    .map(slice -> this.calculateDistance(slice, distanceCalculatorSettings))
                    .reduce(DISTANCE_IN_CASE_NO_TRACK_SLICES, Double::sum);
        } finally {
            final long after = currentTimeMillis();
            out.println("Calculation distance by reducing: " + MILLISECONDS.toSeconds(after - before));
        }
    }

    @Value
    private static class TrackSlice {
        LatLngAlt firstPoint;
        LatLngAlt secondPoint;
        boolean locatedInCity;
    }
}
