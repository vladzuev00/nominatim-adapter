package by.aurorasoft.nominatim.crud.service;

import by.aurorasoft.nominatim.crud.mapper.CityMapper;
import by.aurorasoft.nominatim.crud.model.dto.City;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import by.aurorasoft.nominatim.crud.repository.CityRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityService extends AbsServiceCRUD<Long, CityEntity, City, CityRepository> {

    public CityService(CityMapper mapper, CityRepository repository) {
        super(mapper, repository);
    }

    public List<City> findAll() {
        final List<CityEntity> foundEntities = super.repository.findAll();
        return super.mapper.toDtos(foundEntities);
    }

    public boolean isExistByGeometry(Geometry geometry) {
        return super.repository.isExistByGeometry(geometry);
    }
}
