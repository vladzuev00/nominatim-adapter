package by.aurorasoft.nominatim.crud.repository;

import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

}
