package by.aurorasoft.nominatim.crud.service;

import by.aurorasoft.nominatim.crud.mapper.SearchingCitiesProcessMapper;
import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity;
import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status;
import by.aurorasoft.nominatim.crud.repository.SearchingCitiesProcessRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchingCitiesProcessService
        extends AbsServiceCRUD<Long, SearchingCitiesProcessEntity, SearchingCitiesProcess, SearchingCitiesProcessRepository> {

    public SearchingCitiesProcessService(SearchingCitiesProcessMapper mapper,
                                         SearchingCitiesProcessRepository repository) {
        super(mapper, repository);
    }

    public void updateStatus(SearchingCitiesProcess process, Status newStatus) {
        super.repository.updateStatus(process.getId(), newStatus);
    }

    public void increaseHandledPoints(SearchingCitiesProcess process, long delta) {
        super.repository.increaseHandledPoints(process.getId(), delta);
    }
}
