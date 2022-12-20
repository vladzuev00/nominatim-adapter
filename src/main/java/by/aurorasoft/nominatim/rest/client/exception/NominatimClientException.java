package by.aurorasoft.nominatim.rest.client.exception;

public final class NominatimClientException extends RuntimeException {
    public NominatimClientException() {
        super();
    }

    public NominatimClientException(String description) {
        super(description);
    }

    public NominatimClientException(Exception cause) {
        super(cause);
    }

    public NominatimClientException(String description, Exception cause) {
        super(description, cause);
    }
}
