package by.aurorasoft.distanceclassifier.testutil;

import by.nhorushko.crudgeneric.v2.domain.IdEntity;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public final class IdUtil {

    public static <ID> List<ID> mapToIds(final Collection<? extends IdEntity<ID>> entities) {
        return entities.stream()
                .map(IdEntity::getId)
                .toList();
    }
}
