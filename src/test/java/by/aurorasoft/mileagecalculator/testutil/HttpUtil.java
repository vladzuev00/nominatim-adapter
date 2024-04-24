package by.aurorasoft.mileagecalculator.testutil;

import lombok.experimental.UtilityClass;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.skyscreamer.jsonassert.Customization.customization;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.http.HttpMethod.*;
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

    public static <R> R getExpectingOk(final TestRestTemplate restTemplate,
                                       final String url,
                                       final Class<R> responseType) {
        return getExpectingStatus(restTemplate, url, responseType, OK);
    }

    public static <R> R getExpectingNotAcceptable(final TestRestTemplate restTemplate,
                                                  final String url,
                                                  final Class<R> responseType) {
        return getExpectingStatus(restTemplate, url, responseType, NOT_ACCEPTABLE);
    }

    public static <B, R> R postExpectingOk(final TestRestTemplate restTemplate,
                                           final String url,
                                           final B body,
                                           final Class<R> responseType) {
        return postExpectingStatus(restTemplate, url, body, responseType, OK);
    }

    public static <B, R> R postExpectingNotAcceptable(final TestRestTemplate restTemplate,
                                                      final String url,
                                                      final B body,
                                                      final Class<R> responseType) {
        return postExpectingStatus(restTemplate, url, body, responseType, NOT_ACCEPTABLE);
    }

    public static <B> void postExpectingNoContext(final TestRestTemplate restTemplate, final String url, final B body) {
        postExpectingStatus(restTemplate, url, body, Void.class, NO_CONTENT);
    }

    public static <B, R> R putExpectingOk(final TestRestTemplate restTemplate,
                                          final String url,
                                          final B body,
                                          final Class<R> responseType) {
        return putExpectingStatus(restTemplate, url, body, responseType, OK);
    }

    public static <B, R> R putExpectingNotAcceptable(final TestRestTemplate restTemplate,
                                                     final String url,
                                                     final B body,
                                                     final Class<R> responseType) {
        return putExpectingStatus(restTemplate, url, body, responseType, NOT_ACCEPTABLE);
    }

    public static void deleteExpectingNoContent(final TestRestTemplate restTemplate, final String url) {
        exchangeExpectingStatus(restTemplate, url, DELETE, Void.class, NO_CONTENT);
    }

    private static <R> R getExpectingStatus(final TestRestTemplate restTemplate,
                                            final String url,
                                            final Class<R> responseType,
                                            final HttpStatus status) {
        return exchangeExpectingStatus(restTemplate, url, GET, responseType, status);
    }

    private static <B, R> R postExpectingStatus(final TestRestTemplate restTemplate,
                                                final String url,
                                                final B body,
                                                final Class<R> responseType,
                                                final HttpStatus status) {
        return exchangeExpectingStatus(restTemplate, url, POST, body, responseType, status);
    }

    private static <B, R> R putExpectingStatus(final TestRestTemplate restTemplate,
                                               final String url,
                                               final B body,
                                               final Class<R> responseType,
                                               final HttpStatus status) {
        return exchangeExpectingStatus(restTemplate, url, PUT, body, responseType, status);
    }

    private static <R> R exchangeExpectingStatus(final TestRestTemplate restTemplate,
                                                 final String url,
                                                 final HttpMethod method,
                                                 final Class<R> responseType,
                                                 final HttpStatus status) {
        final HttpEntity<?> httpEntity = createHttpEntity();
        return exchangeExpectingStatus(restTemplate, url, method, httpEntity, responseType, status);
    }

    private static <B, R> R exchangeExpectingStatus(final TestRestTemplate restTemplate,
                                                    final String url,
                                                    final HttpMethod method,
                                                    final B body,
                                                    final Class<R> responseType,
                                                    final HttpStatus status) {
        final HttpEntity<?> httpEntity = createHttpEntity(body);
        return exchangeExpectingStatus(restTemplate, url, method, httpEntity, responseType, status);
    }

    private static <R> R exchangeExpectingStatus(final TestRestTemplate restTemplate,
                                                 final String url,
                                                 final HttpMethod method,
                                                 final HttpEntity<?> httpEntity,
                                                 final Class<R> responseType,
                                                 final HttpStatus status) {
        final ResponseEntity<R> responseEntity = restTemplate.exchange(url, method, httpEntity, responseType);
        assertSame(status, responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    private static <T> HttpEntity<T> createHttpEntity() {
        return createHttpEntity(null);
    }

    private static <T> HttpEntity<T> createHttpEntity(final T body) {
        final HttpHeaders httpHeaders = createHttpHeaders();
        return new HttpEntity<>(body, httpHeaders);
    }

    private static HttpHeaders createHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MEDIA_TYPE);
        headers.setAccept(singletonList(MEDIA_TYPE));
        return headers;
    }
}
