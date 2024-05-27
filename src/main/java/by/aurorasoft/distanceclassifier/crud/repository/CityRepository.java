package by.aurorasoft.distanceclassifier.crud.repository;

import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity.CityGeometry;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    @Query("SELECT e FROM CityEntity e")
    Stream<CityEntity> streamAll();

    @Query("SELECT e.geometry FROM CityEntity e")
    Stream<CityGeometry> findGeometries();

    @Query(value = "SELECT e.geometry FROM CityEntity e WHERE intersects(e.geometry.boundingBox, :line) = true")
    Stream<CityGeometry> findIntersectedGeometries(final LineString line);
}
