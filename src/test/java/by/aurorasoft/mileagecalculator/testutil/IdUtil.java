package by.aurorasoft.mileagecalculator.testutil;

import by.nhorushko.crudgeneric.v2.domain.IdEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public final class IdUtil {

    public static <ID extends Comparable<ID>> List<ID> mapToSortedIds(final Stream<? extends IdEntity<ID>> stream) {
        return stream.map(IdEntity::getId).sorted().toList();
    }

    public static <ID extends Comparable<ID>> List<ID> mapToSortedIds(final Collection<? extends IdEntity<ID>> entities) {
        return mapToSortedIds(entities.stream());
    }

    public static <ID extends Comparable<ID>> List<ID> mapToSortedIds(final Page<? extends IdEntity<ID>> entities) {
        return mapToSortedIds(entities.stream());
    }
}
