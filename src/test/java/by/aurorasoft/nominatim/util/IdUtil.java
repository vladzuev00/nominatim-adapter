package by.aurorasoft.nominatim.util;

import by.nhorushko.crudgeneric.v2.domain.IdEntity;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public final class IdUtil {

    public static <ID> List<ID> mapToIds(final Stream<? extends IdEntity<ID>> stream) {
        return stream.map(IdEntity::getId).toList();
    }

    public static <ID> List<ID> mapToIds(final Collection<? extends IdEntity<ID>> entities) {
        return mapToIds(entities.stream());
    }
}
