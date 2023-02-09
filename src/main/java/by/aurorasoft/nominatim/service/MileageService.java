package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.MileageResponse;
import by.nhorushko.distancecalculator.*;
import by.nhorushko.trackfilter.TrackFilter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.locationtech.jts.geom.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.rangeClosed;

@Service
@RequiredArgsConstructor
public final class MileageService {
    private static final double EPSILON_TO_FILTER_TRACK_POINTS = 0.00015;
    private static final double DISTANCE_IN_CASE_NO_TRACK_SLICES = 0.;

    private final TrackFilter trackFilter;
    private final DistanceCalculator distanceCalculator;
    private final CityService cityService;
    private final GeometryCreatingService geometryByLatLngAltFactory;

    public MileageResponse findMileage(MileageRequest request) {
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
                                collectingAndThen(
                                        toList(),
                                        slices -> this.calculateDistance(slices, distanceCalculatorSettings)
                                )
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
            final List<City> cities = this.findCitiesByPoints(trackPoints);
            final int indexPenultimatePoint = trackPoints.size() - 2;
            return rangeClosed(0, indexPenultimatePoint)
                    .parallel()
                    .mapToObj(i -> Pair.of(trackPoints.get(i), trackPoints.get(i + 1)))
                    .map(slicePoints -> new TrackSlice(
                            slicePoints.getFirst(),
                            slicePoints.getSecond(),
                            //slices, which is located in city, must have second point, which is located in city
                            this.isAnyCityContainPoint(slicePoints.getSecond(), cities)
                    ))
                    .collect(toList());
        }
        finally {
            long after = currentTimeMillis();
            out.println("Finding track slices: " + MILLISECONDS.toSeconds(after - before));
        }
    }

    private List<City> findCitiesByPoints(List<? extends LatLngAlt> trackPoints) {
        final List<? extends LatLngAlt> significantTrackPointsToCreateLineString = this.trackFilter.filter(
                trackPoints, EPSILON_TO_FILTER_TRACK_POINTS);
        final LineString lineString = this.geometryByLatLngAltFactory.createLineString(
                significantTrackPointsToCreateLineString);
        return this.cityService.findCitiesIntersectedByLineStringButNotTouches(lineString);
    }

    private boolean isAnyCityContainPoint(LatLngAlt latLngAlt, List<City> cities) {
        /*
        final Point point = this.geometryCreatingService.createPoint(latLngAlt);
        final GeometryCollection geometryCollection = this.geometryCreatingService.createByCities(cities);
        return geometryCollection.contains(point);
         */
        final Point point = this.geometryByLatLngAltFactory.createPoint(latLngAlt);
        return cities.stream()
                .anyMatch(city -> isPointInsideCity(point, city));
    }

    private static boolean isPointInsideCity(Point point, City city) {
        return city.getGeometry().contains(point);
    }

    private double calculateDistance(List<TrackSlice> trackSlices,
                                     DistanceCalculatorSettings distanceCalculatorSettings) {
        final long before = currentTimeMillis();
        try {
            return trackSlices.stream()
                    .reduce(
                            DISTANCE_IN_CASE_NO_TRACK_SLICES,
                            (partialDistance, nextSlice) ->
                                    partialDistance + this.calculateDistance(nextSlice, distanceCalculatorSettings),
                            Double::sum
                    );
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
