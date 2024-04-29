package by.aurorasoft.distanceclassifier.crud.mapper;

import by.aurorasoft.distanceclassifier.crud.model.dto.City;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class CityMapper extends AbsMapperEntityDto<CityEntity, City> {

    public CityMapper(final ModelMapper modelMapper) {
        super(modelMapper, CityEntity.class, City.class);
    }

    @Override
    protected City create(final CityEntity entity) {
        return new City(entity.getId(), entity.getName(), entity.getType(), getDtoGeometry(entity));
    }

    private static City.CityGeometry getDtoGeometry(final CityEntity entity) {
        final CityEntity.CityGeometry source = entity.getGeometry();
        return new City.CityGeometry(source.getGeometry(), source.getBoundingBox());
    }
}
