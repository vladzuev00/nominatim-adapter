package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader;

import by.aurorasoft.distanceclassifier.model.CityMap;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.simplifier.TrackSimplifier;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

@RequiredArgsConstructor
public abstract class TrackCityMapLoader {
    private final TrackSimplifier trackSimplifier;
    private final GeometryService geometryService;

    @Transactional(readOnly = true)
    public CityMap load(final Track track) {
        final Set<PreparedGeometry> cityGeometries = loadCityGeometries(track);
        final PreparedGeometry scannedGeometry = loadScannedGeometry();
        return new CityMap(cityGeometries, scannedGeometry);
    }

    protected abstract Stream<PreparedGeometry> loadCityGeometries(final LineString line);

    protected abstract PreparedGeometry loadScannedGeometry();

    private Set<PreparedGeometry> loadCityGeometries(final Track track) {
        final Track simplifiedTrack = trackSimplifier.simplify(track);
        final LineString line = geometryService.createLine(simplifiedTrack);
        try (final Stream<PreparedGeometry> geometries = loadCityGeometries(line)) {
            return geometries.collect(toUnmodifiableSet());
        }
    }
}
