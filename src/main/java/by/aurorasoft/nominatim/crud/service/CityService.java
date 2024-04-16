package by.aurorasoft.nominatim.crud.service;

import by.aurorasoft.nominatim.crud.mapper.CityMapper;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import by.aurorasoft.nominatim.crud.repository.CityRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;

@Service
public class CityService extends AbsServiceCRUD<Long, CityEntity, City, CityRepository> {
    private static final String TUPLE_ALIAS_BOUNDING_BOX = "boundingBox";
    private static final String TUPLE_ALIAS_GEOMETRY = "geometry";

    public CityService(final CityMapper mapper, final CityRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public List<City> findAll(final Pageable pageable) {
        final Page<CityEntity> page = repository.findAll(pageable);
        final List<CityEntity> entities = page.getContent();
        return mapper.toDtos(entities);
    }

    @Transactional(readOnly = true)
    public Map<PreparedGeometry, PreparedGeometry> findPreparedGeometriesByPreparedBoundingBoxes() {
        return repository.findBoundingBoxesWithGeometries()
                .stream()
                .collect(toMap(CityService::getPreparedBoundingBox, CityService::getPreparedGeometry));
    }

    @Transactional(readOnly = true)
    public List<PreparedGeometry> findIntersectedPreparedGeometries(final LineString line) {
        try (final Stream<CityEntity> entityStream = repository.findIntersectedCities(line)) {
            return entityStream.map(CityEntity::getGeometry)
                    .map(PreparedGeometryFactory::prepare)
                    .toList();
        }
    }

    private static PreparedGeometry getPreparedBoundingBox(final Tuple tuple) {
        return getPreparedGeometry(tuple, TUPLE_ALIAS_BOUNDING_BOX);
    }

    private static PreparedGeometry getPreparedGeometry(final Tuple tuple) {
        return getPreparedGeometry(tuple, TUPLE_ALIAS_GEOMETRY);
    }

    private static PreparedGeometry getPreparedGeometry(final Tuple tuple, final String alias) {
        final Geometry geometry = (Geometry) tuple.get(alias);
        return prepare(geometry);
    }
}
