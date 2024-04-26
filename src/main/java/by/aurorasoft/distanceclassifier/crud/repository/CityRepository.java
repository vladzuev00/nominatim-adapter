package by.aurorasoft.distanceclassifier.crud.repository;

import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
import by.aurorasoft.distanceclassifier.crud.repository.view.CityGeometryMapView;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;
import java.util.stream.Stream;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    //TODO: remove
    //TIDO: return GeometryWithBoundingBoxView
    @Query("SELECT e.boundingBox AS boundingBox, e.geometry AS geometry FROM CityEntity e")
    List<Tuple> findBoundingBoxesWithGeometries();

    //TODO: remove
    @Query(
            value = "SELECT id, name, geometry, type, bounding_box FROM city WHERE ST_Intersects(bounding_box, :line)",
            nativeQuery = true
    )
    Stream<CityEntity> findIntersectedCities(final LineString line);

    @Query(
            value = "SELECT "
                    + "ST_Union((SELECT ARRAY(SELECT geometry FROM city))) AS unionGeometries, "
                    + "ST_Union((SELECT ARRAY(SELECT bounding_box FROM city))) AS unionBoundingBoxes",
            nativeQuery = true
    )
    CityGeometryMapView findCityGeometryMap();

    @Query(
            value = "SELECT "
                    + "ST_Union((SELECT ARRAY(SELECT geometry FROM city WHERE ST_Intersects(bounding_box, :line)))) AS unionGeometries, "
                    + "ST_Union((SELECT ARRAY(SELECT bounding_box FROM city WHERE ST_Intersects(bounding_box, :line)))) AS unionBoundingBoxes",
            nativeQuery = true
    )
    CityGeometryMapView findCityGeometryMap(final LineString line);
}
