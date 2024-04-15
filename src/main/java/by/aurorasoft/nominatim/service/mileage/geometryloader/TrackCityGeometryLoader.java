package by.aurorasoft.nominatim.service.mileage.geometryloader;

import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.service.mileage.GeometryService;
import by.aurorasoft.nominatim.service.mileage.TrackSimplifier;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;

import java.util.List;

@RequiredArgsConstructor
public abstract class TrackCityGeometryLoader {
    private final TrackSimplifier trackSimplifier;
    private final GeometryService geometryService;

    public final List<PreparedGeometry> load(final Track track) {
        final LineString line = createSimplifiedLine(track);
        return load(line);
    }

    protected abstract List<PreparedGeometry> load(final LineString line);

    private LineString createSimplifiedLine(final Track track) {
        final Track simplifiedTrack = trackSimplifier.simplify(track);
        return geometryService.createLine(simplifiedTrack);
    }
}
