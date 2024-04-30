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
        final ClassifiedDistance gps = new ClassifiedDistance(gpsUrban, gpsCountry);
        final ClassifiedDistance odometer = new ClassifiedDistance(odometerUrban, odometerCountry);
        return new ClassifiedDistanceStorage(gps, odometer);
    }

    private void accumulateAsUrban(final TrackPoint point) {
        gpsUrban += getGpsDelta(point);
        odometerUrban += getOdometerDelta(point);
    }

    private void accumulateAsCountry(final TrackPoint point) {
        gpsCountry += getGpsDelta(point);
        odometerCountry += getOdometerDelta(point);
    }

    private double getGpsDelta(final TrackPoint point) {
        return point.getGpsDistance().getRelative();
    }

    private double getOdometerDelta(final TrackPoint point) {
        return point.getOdometerDistance().getRelative();
    }
}
