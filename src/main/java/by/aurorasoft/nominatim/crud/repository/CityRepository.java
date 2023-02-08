package by.aurorasoft.nominatim.crud.repository;

import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM city WHERE ST_Equals(city.geometry, :geometry))",
            nativeQuery = true)
    boolean isExistByGeometry(Geometry geometry);

    @Query(value = "SELECT id, name, geometry, type FROM city WHERE ST_Intersects(geometry, :lineString) "
            + "AND NOT ST_Touches(geometry, :lineString)",
            nativeQuery = true)
    List<CityEntity> findCitiesIntersectedByLineStringButNotTouches(LineString lineString);
}
