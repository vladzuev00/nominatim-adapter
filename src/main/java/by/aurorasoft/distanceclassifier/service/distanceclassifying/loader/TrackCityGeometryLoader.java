package by.aurorasoft.distanceclassifier.service.distanceclassifying.loader;

import by.aurorasoft.distanceclassifier.model.PreparedBoundedGeometry;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.simplifier.TrackSimplifier;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;

import java.util.Set;

@RequiredArgsConstructor
public abstract class TrackCityGeometryLoader {
    private final TrackSimplifier trackSimplifier;
    private final GeometryService geometryService;

    public final Set<PreparedBoundedGeometry> load(final Track track) {
        final Track simplifiedTrack = trackSimplifier.simplify(track);
        final LineString line = geometryService.createLine(simplifiedTrack);
        return loadInternal(line);
    }

    protected abstract Set<PreparedBoundedGeometry> loadInternal(final LineString line);
}