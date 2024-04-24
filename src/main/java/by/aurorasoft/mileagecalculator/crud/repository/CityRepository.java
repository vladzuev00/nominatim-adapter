package by.aurorasoft.mileagecalculator.crud.repository;

import by.aurorasoft.mileagecalculator.crud.model.entity.CityEntity;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;
import java.util.stream.Stream;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    @Query("SELECT e.boundingBox AS boundingBox, e.geometry AS geometry FROM CityEntity e")
    List<Tuple> findBoundingBoxesWithGeometries();

    @Query(
            value = "SELECT id, name, geometry, type, bounding_box FROM city WHERE ST_Intersects(bounding_box, :line)",
            nativeQuery = true
    )
    Stream<CityEntity> findIntersectedCities(final LineString line);
}
