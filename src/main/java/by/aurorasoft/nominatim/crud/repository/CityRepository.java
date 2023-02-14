package by.aurorasoft.nominatim.crud.repository;

import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM city WHERE ST_Equals(city.geometry, :geometry))",
            nativeQuery = true)
    boolean isExistByGeometry(Geometry geometry);

    @Query("SELECT ce.boundingBox AS boundingBox, ce.geometry AS geometry FROM CityEntity ce")
    List<Tuple> findBoundingBoxesWithGeometries();
}
