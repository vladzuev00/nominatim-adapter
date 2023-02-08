package by.aurorasoft.nominatim.service;

import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.model.MileageRequest;
import by.aurorasoft.nominatim.rest.model.MileageResponse;
import by.aurorasoft.nominatim.service.factory.GeometryByLatLngAltFactory;
import by.nhorushko.distancecalculator.*;
import by.nhorushko.trackfilter.TrackFilter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final GeometryByLatLngAltFactory geometryByLatLngAltFactory;

    public MileageResponse findMileage(MileageRequest request) {
        final DistanceCalculatorSettings distanceCalculatorSettings = new DistanceCalculatorSettingsImpl(
                request.getMinDetectionSpeed(), request.getMaxMessageTimeout());
        final List<? extends LatLngAlt> significantTrackPoints = this.trackFilter.filter(request.getTrackPoints(),
                EPSILON_TO_FILTER_TRACK_POINTS);
        final List<TrackSlice> trackSlices = this.findTrackSlices(significantTrackPoints);
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

    private List<TrackSlice> findTrackSlices(List<? extends LatLngAlt> points) {
        final List<City> cities = this.findCitiesByPoints(points);
        final int indexPenultimatePoint = points.size() - 2;
        return rangeClosed(0, indexPenultimatePoint)
                .mapToObj(i -> Pair.of(points.get(i), points.get(i + 1)))
                .map(slicePoints -> new TrackSlice(
                        slicePoints.getFirst(),
                        slicePoints.getSecond(),
                        //slices, which is located in city, must have second point, which is located in city
                        this.isAnyCityContainPoint(slicePoints.getSecond(), cities)
                ))
                .collect(toList());
    }

    private List<City> findCitiesByPoints(List<? extends LatLngAlt> points) {
        final LineString lineString = this.geometryByLatLngAltFactory.createLineString(points);
        return this.cityService.findCitiesIntersectedByLineStringButNotTouches(lineString);
    }

    private boolean isAnyCityContainPoint(LatLngAlt latLngAlt, List<City> cities) {
        final Point point = this.geometryByLatLngAltFactory.createPoint(latLngAlt);
        return cities.stream()
                .anyMatch(city -> isPointInsideCity(point, city));
    }

    private static boolean isPointInsideCity(Point point, City city) {
        return city.getGeometry().contains(point);
    }

    private double calculateDistance(List<TrackSlice> trackSlices,
                                     DistanceCalculatorSettings distanceCalculatorSettings) {
        return trackSlices.stream()
                .parallel()
                .reduce(
                        DISTANCE_IN_CASE_NO_TRACK_SLICES,
                        (partialDistance, nextSlice) ->
                                partialDistance + this.calculateDistance(nextSlice, distanceCalculatorSettings),
                        Double::sum
                );
    }

    @Value
    private static class TrackSlice {
        LatLngAlt firstPoint;
        LatLngAlt secondPoint;
        boolean locatedInCity;
    }
}
