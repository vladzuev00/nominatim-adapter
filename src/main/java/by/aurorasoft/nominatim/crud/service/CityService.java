package by.aurorasoft.nominatim.crud.service;

import by.aurorasoft.nominatim.crud.mapper.CityMapper;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import by.aurorasoft.nominatim.crud.repository.CityRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.locationtech.jts.geom.prep.PreparedGeometryFactory.prepare;

@Service
@Transactional
public class CityService extends AbsServiceCRUD<Long, CityEntity, City, CityRepository> {
    private static final String TUPLE_ALIAS_OF_BOUNDING_BOX = "boundingBox";
    private static final String TUPLE_ALIAS_OF_GEOMETRY = "geometry";

    public CityService(CityMapper mapper, CityRepository repository) {
        super(mapper, repository);
    }

    public List<City> findAll(int pageNumber, int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        final Page<CityEntity> page = super.repository.findAll(pageable);
        final List<CityEntity> foundEntities = page.getContent();
        return super.mapper.toDtos(foundEntities);
    }

    public boolean isExistByGeometry(Geometry geometry) {
        return super.repository.isExistByGeometry(geometry);
    }

    public Map<PreparedGeometry, PreparedGeometry> findPreparedGeometriesByBoundingBoxes() {
        final List<Tuple> geometriesWithBoundingBoxes = super.repository.findGeometriesWithBoundingBoxes();
        return geometriesWithBoundingBoxes
                .stream()
                .collect(
                        toMap(
                                geometryWithBoundingBox -> prepare(
                                        (Geometry) geometryWithBoundingBox.get(TUPLE_ALIAS_OF_BOUNDING_BOX)
                                ),
                                geometryWithBoundingBox -> prepare(
                                        (Geometry) geometryWithBoundingBox.get(TUPLE_ALIAS_OF_GEOMETRY)
                                )
                        )
                );
    }
}
