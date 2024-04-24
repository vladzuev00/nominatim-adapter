package by.aurorasoft.mileagecalculator.service.mileage.loader;

import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.service.geometry.GeometryService;
import by.aurorasoft.mileagecalculator.service.mileage.simplifier.TrackSimplifier;
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
