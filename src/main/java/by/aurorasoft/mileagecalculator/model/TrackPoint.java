package by.aurorasoft.mileagecalculator.model;

import by.nhorushko.classifieddistance.Distance;
import lombok.Value;

@Value
public class TrackPoint {
    Coordinate coordinate;
    int speed;
    Distance gpsDistance;
    Distance odometerDistance;
}
