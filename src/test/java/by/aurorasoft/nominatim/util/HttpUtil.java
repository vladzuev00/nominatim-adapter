package by.aurorasoft.nominatim.util;

import lombok.experimental.UtilityClass;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.skyscreamer.jsonassert.Customization.customization;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@UtilityClass
public final class HttpUtil {
    private static final String JSON_PROPERTY_NAME_DATE_TIME = "dateTime";
    private static final MediaType MEDIA_TYPE = APPLICATION_JSON;

    public static final CustomComparator JSON_COMPARATOR_IGNORING_DATE_TIME = new CustomComparator(
            STRICT,
            customization(JSON_PROPERTY_NAME_DATE_TIME, (first, second) -> true)
    );

    public <B, R> R postExpectingOk(final TestRestTemplate restTemplate,
                                    final String url,
                                    final B body,
                                    final Class<R> resultType) {
        final HttpEntity<B> httpEntity = createHttpEntity(body);
        final ResponseEntity<R> response = restTemplate.postForEntity(url, httpEntity, resultType);
        assertSame(OK, response.getStatusCode());
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
