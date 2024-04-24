package by.aurorasoft.nominatim.testutil;

import lombok.experimental.UtilityClass;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.skyscreamer.jsonassert.Customization.customization;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@UtilityClass
public final class HttpUtil {
    private static final String JSON_PATH_DATE_TIME = "dateTime";
    private static final MediaType MEDIA_TYPE = APPLICATION_JSON;

    public static final CustomComparator JSON_COMPARATOR_IGNORING_DATE_TIME = new CustomComparator(
            STRICT,
            customization(JSON_PATH_DATE_TIME, (first, second) -> true)
    );

    public static <R> R getExpectingOk(final TestRestTemplate restTemplate, final String url, final Class<R> resultType) {
        final ResponseEntity<R> entity = restTemplate.getForEntity(url, resultType);
        assertSame(OK, entity.getStatusCode());
        return entity.getBody();
    }

    public static <R> R getExpectingNotAcceptable(final TestRestTemplate restTemplate, final String url, final Class<R> resultType) {
        final ResponseEntity<R> entity = restTemplate.getForEntity(url, resultType);
        assertSame(NOT_ACCEPTABLE, entity.getStatusCode());
        return entity.getBody();
    }

    public static <B> void postExpectingNoContext(final TestRestTemplate restTemplate, final String url, final B body) {
        postExpectingStatus(restTemplate, url, body, Void.class, NO_CONTENT);
    }

    public static <B, R> R postExpectingOk(final TestRestTemplate restTemplate,
                                           final String url,
                                           final B body,
                                           final Class<R> resultType) {
        return postExpectingStatus(restTemplate, url, body, resultType, OK);
    }

    public static <B, R> R postExpectingNotAcceptable(final TestRestTemplate restTemplate,
                                                      final String url,
                                                      final B body,
                                                      final Class<R> resultType) {
        return postExpectingStatus(restTemplate, url, body, resultType, NOT_ACCEPTABLE);
    }

    public static <B, R> R putExpectingOk(final TestRestTemplate restTemplate,
                                          final String url,
                                          final B body,
                                          final Class<R> resultType) {
        final HttpEntity<B> httpEntity = createHttpEntity(body);
        final ResponseEntity<R> response = restTemplate.exchange(url, PUT, httpEntity, resultType);
        assertSame(OK, response.getStatusCode());
        return response.getBody();
    }

    public static <B, R> R putExpectingNotAcceptable(final TestRestTemplate restTemplate,
                                                     final String url,
                                                     final B body,
                                                     final Class<R> resultType) {
        final HttpEntity<B> httpEntity = createHttpEntity(body);
        final ResponseEntity<R> response = restTemplate.exchange(url, PUT, httpEntity, resultType);
        assertSame(NOT_ACCEPTABLE, response.getStatusCode());
        return response.getBody();
    }

    public static void deleteExpectingNoContent(final TestRestTemplate restTemplate, final String url) {
        final ResponseEntity<Object> response = restTemplate.exchange(url, DELETE, new HttpEntity<>(createHttpHeaders()), Object.class);
        assertSame(NO_CONTENT, response.getStatusCode());
    }

    private static <B, R> R postExpectingStatus(final TestRestTemplate restTemplate,
                                                final String url,
                                                final B body,
                                                final Class<R> resultType,
                                                final HttpStatus status) {
        final HttpEntity<B> httpEntity = createHttpEntity(body);
        final ResponseEntity<R> response = restTemplate.postForEntity(url, httpEntity, resultType);
        assertSame(status, response.getStatusCode());
        return response.getBody();
    }

    private static <T> HttpEntity<T> createHttpEntity(final T body) {
        final HttpHeaders headers = createHttpHeaders();
        return new HttpEntity<>(body, headers);
    }

    private static HttpHeaders createHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MEDIA_TYPE);
        headers.setAccept(singletonList(MEDIA_TYPE));
        return headers;
    }
}
