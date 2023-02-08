package by.aurorasoft.nominatim.crud.service;

import by.aurorasoft.nominatim.crud.mapper.CityMapper;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import by.aurorasoft.nominatim.crud.repository.CityRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityService extends AbsServiceCRUD<Long, CityEntity, City, CityRepository> {

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

    public List<City> findCitiesIntersectedByLineStringButNotTouches(LineString lineString) {
        final List<CityEntity> foundEntities = super.repository.findCitiesIntersectedByLineStringButNotTouches(lineString);
        return super.mapper.toDtos(foundEntities);
    }
}
