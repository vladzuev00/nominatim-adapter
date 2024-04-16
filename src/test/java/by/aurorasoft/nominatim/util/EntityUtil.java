package by.aurorasoft.nominatim.util;

import by.aurorasoft.nominatim.crud.model.entity.BaseEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public final class EntityUtil {

    public static <ID> List<ID> mapToIds(final Stream<? extends BaseEntity<ID>> stream) {
        return stream.map(BaseEntity::getId).toList();
    }
}
