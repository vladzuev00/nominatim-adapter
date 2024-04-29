package by.aurorasoft.distanceclassifier.crud.service;

import by.aurorasoft.distanceclassifier.crud.mapper.CityMapper;
import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
import by.aurorasoft.distanceclassifier.crud.repository.CityRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class CityService extends AbsServiceCRUD<Long, CityEntity, City, CityRepository> {

    public CityService(final CityMapper mapper, final CityRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<City> findAll(final Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Stream<CityGeometry> findGeometries() {
        return findGeometries(repository::findGeometries);
    }

    @Transactional(readOnly = true)
    public Stream<CityGeometry> findIntersectedGeometries(final LineString line) {
        return findGeometries(() -> repository.findIntersectedGeometries(line));
    }

    private Stream<CityGeometry> findGeometries(final Supplier<Stream<CityEntity.CityGeometry>> supplier) {
        return supplier.get().map(geometry -> ((CityMapper) mapper).mapToDtoGeometry(geometry));
    }
}
