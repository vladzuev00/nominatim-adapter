package by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.pointlocator.TrackPointLocator;
import by.nhorushko.classifieddistance.ClassifiedDistance;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ClassifiedDistanceAccumulator {
    private final TrackPointLocator pointLocator;
    private double gpsUrban;
    private double gpsCountry;
    private double odometerUrban;
    private double odometerCountry;

    public void accumulate(final TrackPoint point) {
        if (pointLocator.isCity(point)) {
            accumulateAsUrban(point);
        } else {
            accumulateAsCountry(point);
        }
    }

    public ClassifiedDistanceStorage get() {
        return new ClassifiedDistanceStorage(getGpsDistance(), getOdometerDistance());
    }

    private void accumulateAsUrban(final TrackPoint point) {
        gpsUrban += point.getGpsDistance().getRelative();
        odometerUrban += point.getOdometerDistance().getRelative();
    }

    private void accumulateAsCountry(final TrackPoint point) {
        gpsCountry += point.getGpsDistance().getRelative();
        odometerCountry += point.getOdometerDistance().getRelative();
    }

    private ClassifiedDistance getGpsDistance() {
        return new ClassifiedDistance(gpsUrban, gpsCountry);
    }

    private ClassifiedDistance getOdometerDistance() {
        return new ClassifiedDistance(odometerUrban, odometerCountry);
    }
}
