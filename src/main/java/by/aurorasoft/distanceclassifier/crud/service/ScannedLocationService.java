package by.aurorasoft.distanceclassifier.crud.service;

import by.aurorasoft.distanceclassifier.crud.mapper.ScannedLocationMapper;
import by.aurorasoft.distanceclassifier.crud.model.dto.ScannedLocation;
import by.aurorasoft.distanceclassifier.crud.model.entity.ScannedLocationEntity;
import by.aurorasoft.distanceclassifier.crud.repository.ScannedLocationRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScannedLocationService extends AbsServiceCRUD<Long, ScannedLocationEntity, ScannedLocation, ScannedLocationRepository> {

    public ScannedLocationService(final ScannedLocationMapper mapper, final ScannedLocationRepository repository) {
        super(mapper, repository);
    }

    public void append(final Geometry geometry) {
        repository.append(geometry);
    }

    @Transactional(readOnly = true)
    public ScannedLocation get() {
        final ScannedLocationEntity entity = repository.get();
        return mapper.toDto(entity);
    }
}
