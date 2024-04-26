package by.aurorasoft.mileagecalculator.model;

import lombok.Value;

@Value
public class TrackPoint {
    Coordinate coordinate;
    int speed;
    Distance gpsDistance;
    Distance odometerDistance;

    @Value
    public static class Distance {
        double relative;
        double absolute;
    }
}
