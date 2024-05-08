package by.aurorasoft.distanceclassifier.testutil;

import by.nhorushko.crudgeneric.v2.domain.IdEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

@UtilityClass
public final class IdUtil {

    public static <ID> Set<ID> mapToIds(final Stream<? extends IdEntity<ID>> stream) {
        return stream.map(IdEntity::getId).collect(toUnmodifiableSet());
    }

    public static <ID> Set<ID> mapToIds(final Page<? extends IdEntity<ID>> entities) {
        return mapToIds(entities.stream());
    }
}
