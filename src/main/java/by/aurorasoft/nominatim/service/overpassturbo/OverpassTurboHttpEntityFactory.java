package by.aurorasoft.nominatim.service.overpassturbo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

//@Component
//public final class OverpassTurboHttpEntityFactory {
//    private static final MediaType MEDIA_TYPE = APPLICATION_JSON;
//
//    private final int timeout;
//
//    public OverpassTurboHttpEntityFactory(@Value("${overpass-turbo.timeout}") final int timeout) {
//        this.timeout = timeout;
//    }
//
//    public HttpEntity<String> create() {
//        final HttpHeaders headers = createHeaders();
//        return new HttpEntity<>(body, headers);
//    }
//
//    private static HttpHeaders createHeaders() {
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MEDIA_TYPE);
//        headers.setAccept(singletonList(MEDIA_TYPE));
//        return headers;
//    }
//}
