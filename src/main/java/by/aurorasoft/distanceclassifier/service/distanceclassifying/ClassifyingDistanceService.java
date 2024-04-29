package by.aurorasoft.distanceclassifier.service.distanceclassifying;

import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.loader.TrackCityGeometryLoader;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import by.nhorushko.classifieddistance.ClassifiedDistance;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public final class ClassifyingDistanceService {
    private final TrackCityGeometryLoader trackCityGeometryLoader;
    private final GeometryService geometryService;

    public ClassifiedDistanceStorage classify(final Track track, final int urbanSpeedThreshold) {
        final StorageAccumulator distanceAccumulator = createDistanceAccumulator(track, urbanSpeedThreshold);
        track.getPoints().forEach(distanceAccumulator::accumulate);
        return distanceAccumulator.createStorage();
    }

    private StorageAccumulator createDistanceAccumulator(final Track track, final int urbanSpeedThreshold) {
        final Set<PreparedCityGeometry> cityGeometries = trackCityGeometryLoader.load(track);
        final PointLocator pointLocator = new PointLocator(geometryService, cityGeometries, urbanSpeedThreshold);
        return new StorageAccumulator(pointLocator);
    }

    @RequiredArgsConstructor
    static final class StorageAccumulator {
        private final PointLocator pointLocator;
        private double gpsUrban;
        private double gpsCountry;
        private double odometerUrban;
        private double odometerCountry;

        public void accumulate(final TrackPoint point) {
            if (pointLocator.isUrban(point)) {
                gpsUrban += point.getGpsDistance().getRelative();
                odometerUrban += point.getOdometerDistance().getRelative();
            } else {
                gpsCountry += point.getGpsDistance().getRelative();
                odometerCountry += point.getOdometerDistance().getRelative();
            }
        }

        public ClassifiedDistanceStorage createStorage() {
            return new ClassifiedDistanceStorage(
                    new ClassifiedDistance(gpsUrban, gpsCountry),
                    new ClassifiedDistance(odometerUrban, odometerCountry)
            );
        }
    }

    @RequiredArgsConstructor
    static final class PointLocator {
        private final GeometryService geometryService;
        private final Set<PreparedCityGeometry> cityGeometries;
        private final int urbanSpeedThreshold;

        public boolean isUrban(final TrackPoint point) {
            return isLocatedInCity(point) || (isUnknownLocation(point) && point.getSpeed() <= urbanSpeedThreshold);
        }

        private boolean isLocatedInCity(final TrackPoint point) {
            return geometryService.isAnyGeometryContain(cityGeometries, point);
        }

        private boolean isUnknownLocation(final TrackPoint point) {
            return !geometryService.isAnyBoundingBoxContain(cityGeometries, point);
        }
    }
}
