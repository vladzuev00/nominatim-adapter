package by.aurorasoft.nominatim.service.mileage;

import by.aurorasoft.nominatim.cache.CityGeometryCache;
import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.model.Track;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public final class TrackCityGeometryLoader {
    private final TrackSimplifier trackSimplifier;
    private final CityGeometryCache cityGeometryCache;
    private final CityService cityService;

    public List<PreparedGeometry> load(final Track track) {
        final Track simplifiedTrack = trackSimplifier.simplify(track);
        return cityGeometryCache.isFilled() ?
    }

    private List<PreparedGeometry> loadFromCache(LineString lineString) {
        return this.citiesGeometriesByBoundingBoxes
                .entrySet()
                .stream()
                .filter(geometryByBoundingBox -> geometryByBoundingBox.getKey().intersects(lineString))
                .map(Map.Entry::getValue)
                .collect(toList());
    }

    private List<PreparedGeometry> findIntersectedCityGeometries(final Track track) {
        final LineString line = geometryService.createLine(track);
        return cityGeometryCache.isFilled() ?
        return this.citiesGeometriesByBoundingBoxes != null
                ? this.findGeometriesIntersectedByLineStringByLoadedPreparedGeometries(line)
                : this.cityService.findPreparedGeometriesWhoseBoundingBoxIntersectedByLineString(line);
    }

    private List<PreparedGeometry> findGeometriesIntersectedByLineStringByLoadedPreparedGeometries(LineString lineString) {
        return this.citiesGeometriesByBoundingBoxes
                .entrySet()
                .stream()
                .filter(geometryByBoundingBox -> geometryByBoundingBox.getKey().intersects(lineString))
                .map(Map.Entry::getValue)
                .collect(toList());
    }
}
