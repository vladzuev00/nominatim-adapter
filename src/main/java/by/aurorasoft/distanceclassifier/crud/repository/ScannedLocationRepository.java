package by.aurorasoft.distanceclassifier.crud.repository;

import by.aurorasoft.distanceclassifier.crud.model.entity.ScannedLocationEntity;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ScannedLocationRepository extends JpaRepository<ScannedLocationEntity, Long> {

    @Modifying
    @Query("UPDATE ScannedLocationEntity e SET e.geometry = union(e.geometry, :geometry)")
    void append(final Geometry geometry);

    @Query("SELECT e FROM ScannedLocationEntity e")
    ScannedLocationEntity get();
}
