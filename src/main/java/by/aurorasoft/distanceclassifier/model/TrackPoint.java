package by.aurorasoft.distanceclassifier.model;

import by.nhorushko.classifieddistance.Distance;
import by.nhorushko.trackfilter.LatLng;
import lombok.Value;

//TODO: test
@Value
public class TrackPoint implements LatLng {
    Coordinate coordinate;
    int speed;
    Distance gpsDistance;
    Distance odometerDistance;

    @Override
    public float getLatitude() {
        return (float) coordinate.getLatitude();
    }

    @Override
    public float getLongitude() {
        return (float) coordinate.getLongitude();
    }
}
