package by.aurorasoft.nominatim.util;

import by.aurorasoft.nominatim.crud.model.entity.BaseEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class EntityUtil {

    public static <ID> List<ID> mapToIds(final List<? extends BaseEntity<ID>> entities) {
        return entities.stream()
                .map(BaseEntity::getId)
                .toList();
    }
}
