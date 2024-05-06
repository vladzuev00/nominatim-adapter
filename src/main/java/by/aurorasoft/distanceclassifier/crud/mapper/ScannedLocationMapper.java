package by.aurorasoft.distanceclassifier.crud.mapper;

import by.aurorasoft.distanceclassifier.crud.model.dto.ScannedLocation;
import by.aurorasoft.distanceclassifier.crud.model.entity.ScannedLocationEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class ScannedLocationMapper extends AbsMapperEntityDto<ScannedLocationEntity, ScannedLocation> {

    public ScannedLocationMapper(final ModelMapper modelMapper) {
        super(modelMapper, ScannedLocationEntity.class, ScannedLocation.class);
    }

    @Override
    protected ScannedLocation create(final ScannedLocationEntity entity) {
        return new ScannedLocation(entity.getId(), entity.getGeometry());
    }
}
