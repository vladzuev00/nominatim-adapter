package by.aurorasoft.nominatim.util;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import static by.aurorasoft.nominatim.util.PageRequestUtil.PROPERTY_NAME_ID;
import static by.aurorasoft.nominatim.util.PageRequestUtil.createRequestSortingById;
import static org.junit.Assert.assertEquals;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

public final class PageRequestUtilTest {

    @Test
    public void requestSortingByIdShouldBeCreated() {
        final int givenPageNumber = 0;
        final int givenPageSize = 5;

        final PageRequest actual = createRequestSortingById(givenPageNumber, givenPageSize);
        final PageRequest expected = of(givenPageNumber, givenPageSize).withSort(ASC, PROPERTY_NAME_ID);
        assertEquals(expected, actual);
    }
}
