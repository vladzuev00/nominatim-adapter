package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Integer.MIN_VALUE;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class CityControllerIT extends AbstractContextTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(255, 'Minsk', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), 'CAPITAL')")
    @Sql(statements = "INSERT INTO city(id, name, geometry, type) VALUES(256, 'Mogilev', "
            + "ST_GeomFromText('POLYGON((1 2, 3 5, 5 6, 6 7, 1 2))', 4326), 'REGIONAL')")
    @Sql(statements = "DELETE FROM city WHERE city.id IN (255, 256)", executionPhase = AFTER_TEST_METHOD)
    public void allCitiesShouldBeFound() {
        final int givenPageNumber = 0;
        final int givenPageSize = 3;

        final String url = createUrlToFindAllCities(givenPageNumber, givenPageSize);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(OK, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expected = "{\"pageNumber\":0,\"pageSize\":3,"
                + "\"cities\":[{\"id\":255,\"name\":\"Minsk\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\""
                + ":[[[1.0,2.0],[3.0,4.0],[5.0,6.0],[6.0,7.0],[1.0,2.0]]]},\"type\":\"CAPITAL\"},{\"id\":256,\"name\""
                + ":\"Mogilev\",\"geometry\":{\"type\":\"Polygon\","
                + "\"coordinates\":[[[1.0,2.0],[3.0,5.0],[5.0,6.0],[6.0,7.0],[1.0,2.0]]]},\"type\":\"REGIONAL\"}]}";
        assertEquals(expected, actual);
    }

    @Test
    public void allCitiesShouldNotBeFoundBecauseOfPageNumberIsLessThanAllowableMinimal() {
        final int givenPageNumber = -1;
        final int givenPageSize = 3;

        final String url = createUrlToFindAllCities(givenPageNumber, givenPageSize);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{"
                + "\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"findAll.pageNumber: must be greater than or equal to 0\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\""
                + "}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void allCitiesShouldNotBeFoundBecauseOfPageNumberIsMoreThanAllowableMaximal() {
        final int givenPageNumber = 3000001;
        final int givenPageSize = 3;

        final String url = createUrlToFindAllCities(givenPageNumber, givenPageSize);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{"
                + "\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"findAll.pageNumber: must be less than or equal to 3000000\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\""
                + "}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void allCitiesShouldNotBeFoundBecauseOfPageSizeIsLessThanAllowableMinimal() {
        final int givenPageNumber = 0;
        final int givenPageSize = 0;

        final String url = createUrlToFindAllCities(givenPageNumber, givenPageSize);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{"
                + "\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"findAll.pageSize: must be greater than or equal to 1\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\""
                + "}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void allCitiesShouldNotBeFoundBecauseOfPageSizeIsMoreThanAllowableMaximal() {
        final int givenPageNumber = 0;
        final int givenPageSize = 3000001;

        final String url = createUrlToFindAllCities(givenPageNumber, givenPageSize);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{"
                + "\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"findAll.pageSize: must be less than or equal to 3000000\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\""
                + "}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    private static String createUrlToFindAllCities(int pageNumber, int pageSize) {
        return new UrlToFindAllCitiesBuilder()
                .catalogPageNumber(pageNumber)
                .catalogPageSize(pageSize)
                .build();
    }

    private static final class UrlToFindAllCitiesBuilder {
        private static final String EXCEPTION_DESCRIPTION_URI_BUILDING_BY_NOT_DEFINED_PARAMETERS
                = "Uri was build by not defined parameters.";
        private static final String URL_WITHOUT_PARAMETERS = "/city";

        private static final String PARAM_NAME_PAGE_NUMBER = "pageNumber";
        private static final String PARAM_NAME_PAGE_SIZE = "pageSize";

        private int pageNumber;
        private int pageSize;

        public UrlToFindAllCitiesBuilder() {
            this.pageNumber = MIN_VALUE;
            this.pageSize = MIN_VALUE;
        }

        public UrlToFindAllCitiesBuilder catalogPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public UrlToFindAllCitiesBuilder catalogPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public String build() {
            if (this.pageNumber == MIN_VALUE || this.pageSize == MIN_VALUE) {
                throw new IllegalStateException(EXCEPTION_DESCRIPTION_URI_BUILDING_BY_NOT_DEFINED_PARAMETERS);
            }
            return fromUriString(URL_WITHOUT_PARAMETERS)
                    .queryParam(PARAM_NAME_PAGE_NUMBER, this.pageNumber)
                    .queryParam(PARAM_NAME_PAGE_SIZE, this.pageSize)
                    .build()
                    .toUriString();
        }
    }
}
