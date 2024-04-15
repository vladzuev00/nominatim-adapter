package by.aurorasoft.nominatim.crud.repository;

import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity;
import by.aurorasoft.nominatim.model.SearchingCitiesProcessStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchingCitiesProcessRepository extends JpaRepository<SearchingCitiesProcessEntity, Long> {

    @Modifying
    @Query("UPDATE SearchingCitiesProcessEntity e SET e.status = :newStatus WHERE e.id = :id")
    void updateStatus(Long id, SearchingCitiesProcessStatus newStatus);

    @Modifying
    @Query("UPDATE SearchingCitiesProcessEntity e "
            + "SET e.handledPoints = e.handledPoints + :delta "
            + "WHERE e.id = :id")
    void increaseHandledPoints(Long id, long delta);

    @Query("SELECT e FROM SearchingCitiesProcessEntity e WHERE e.status = :status")
    List<SearchingCitiesProcessEntity> findByStatus(SearchingCitiesProcessStatus status, Pageable pageable);
}
