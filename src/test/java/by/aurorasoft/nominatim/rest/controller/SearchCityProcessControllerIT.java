package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static by.aurorasoft.nominatim.crud.model.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static java.lang.Integer.MIN_VALUE;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SearchCityProcessControllerIT extends AbstractContextTest {

    private static final String CONTROLLER_URL = "/searchCityTask";
    private static final String SLASH = "/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO searching_cities_process "
            + "(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 0.01, 10000, 1000, 'HANDLING')")
    @Sql(statements = "DELETE FROM searching_cities_process", executionPhase = AFTER_TEST_METHOD)
    public void processShouldBeFoundById() {
        final Long givenId = 255L;

        final String url = createUrlToFindProcessById(givenId);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(OK, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expected = "{\"id\":255,\"geometry\":{\"type\":\"Polygon\","
                + "\"coordinates\":[[[1.0,2.0],[3.0,4.0],[5.0,6.0],[6.0,7.0],[1.0,2.0]]]},"
                + "\"searchStep\":0.01,\"totalPoints\":10000,\"handledPoints\":1000,\"status\":\"HANDLING\"}";
        assertEquals(expected, actual);
    }

    @Test
    public void processShouldNotBeFoundByNotExistingId() {
        final Long givenId = 255L;

        final String url = createUrlToFindProcessById(givenId);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_FOUND, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_FOUND\",\"message\":\"Process with id '255' doesn't exist.\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO searching_cities_process "
            + "(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 0.01, 10000, 1000, 'HANDLING')")
    @Sql(statements = "INSERT INTO searching_cities_process "
            + "(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 0.01, 10000, 1000, 'HANDLING')")
    @Sql(statements = "INSERT INTO searching_cities_process "
            + "(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(257, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 0.01, 10000, 10000, 'SUCCESS')")
    @Sql(statements = "INSERT INTO searching_cities_process "
            + "(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(258, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))'), 0.01, 10000, 5000, 'ERROR')")
    @Sql(statements = "DELETE FROM searching_cities_process", executionPhase = AFTER_TEST_METHOD)
    public void processesShouldBeFoundByStatus() {
        final int givenPageNumber = 0;
        final int givenPageSize = 3;

        final String url = createUrlToFindProcessesByStatus(HANDLING, givenPageNumber, givenPageSize);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(OK, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expected = "{\"pageNumber\":0,\"pageSize\":3,\"processes\":[{\"id\":255,"
                + "\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[1.0,2.0],[3.0,4.0],[5.0,6.0],[6.0,7.0],[1.0,2.0]]]},"
                + "\"searchStep\":0.01,\"totalPoints\":10000,\"handledPoints\":1000,\"status\":\"HANDLING\"},"
                + "{\"id\":256,\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[1.0,2.0],[3.0,4.0],[5.0,6.0],[6.0,7.0],[1.0,2.0]]]},"
                + "\"searchStep\":0.01,\"totalPoints\":10000,\"handledPoints\":1000,\"status\":\"HANDLING\"}]}";
        assertEquals(expected, actual);
    }

    @Test
    public void processesShouldNotBeFoundByNotValidStatus() {
        final String url = "/searchCityTask?status=NOTVALID&pageNumber=0&pageSize=3";

        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"'NOTVALID' should be replaced by one of: HANDLING, SUCCESS, ERROR.\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void processesShouldNotBeFoundBecauseOfPageNumberIsLessThanAllowableMinimal() {

    }

    private static String createUrlToFindProcessById(Long id) {
        return CONTROLLER_URL + SLASH + id;
    }

    private static String createUrlToFindProcessesByStatus(Status status, int pageNumber, int pageSize) {
        return new UrlToFindProcessesByStatusBuilder()
                .catalogStatus(status)
                .catalogPageNumber(pageNumber)
                .catalogPageSize(pageSize)
                .build();
    }

    private static final class UrlToFindProcessesByStatusBuilder {
        private static final String EXCEPTION_DESCRIPTION_URI_BUILDING_BY_NOT_DEFINED_PARAMETERS
                = "Url was built by not defined parameters.";

        private static final String PARAM_NAME_STATUS = "status";
        private static final String PARAM_NAME_PAGE_SIZE = "pageSize";
        private static final String PARAM_NAME_PAGE_NUMBER = "pageNumber";

        private Status status;
        private int pageNumber;
        private int pageSize;

        public UrlToFindProcessesByStatusBuilder() {
            this.status = null;
            this.pageNumber = MIN_VALUE;
            this.pageSize = MIN_VALUE;
        }

        public UrlToFindProcessesByStatusBuilder catalogStatus(Status status) {
            this.status = status;
            return this;
        }

        public UrlToFindProcessesByStatusBuilder catalogPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public UrlToFindProcessesByStatusBuilder catalogPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public String build() {
            if (this.status == null || this.pageNumber == MIN_VALUE || this.pageSize == MIN_VALUE) {
                throw new IllegalStateException(EXCEPTION_DESCRIPTION_URI_BUILDING_BY_NOT_DEFINED_PARAMETERS);
            }
            return fromUriString(CONTROLLER_URL)
                    .queryParam(PARAM_NAME_STATUS, this.status)
                    .queryParam(PARAM_NAME_PAGE_NUMBER, this.pageNumber)
                    .queryParam(PARAM_NAME_PAGE_SIZE, this.pageSize)
                    .build()
                    .toUriString();
        }
    }
}
