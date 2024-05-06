package by.aurorasoft.distanceclassifier.model;

import lombok.Value;
import org.locationtech.jts.geom.prep.PreparedGeometry;

import java.util.Set;

@Value
public class CityMap {
    Set<PreparedCityGeometry> cityGeometries;
    PreparedGeometry scannedLocation;
}
