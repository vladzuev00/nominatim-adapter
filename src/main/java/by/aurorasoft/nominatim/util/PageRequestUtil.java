package by.aurorasoft.nominatim.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@UtilityClass
public final class PageRequestUtil {
    private static final String PROPERTY_NAME_ID = "id";

    public static PageRequest createRequestSortingById(final int pageNumber, final int pageSize) {
        return of(pageNumber, pageSize).withSort(ASC, PROPERTY_NAME_ID);
    }
}
