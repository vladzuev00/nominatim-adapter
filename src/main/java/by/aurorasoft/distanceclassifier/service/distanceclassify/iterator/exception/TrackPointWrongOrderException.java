package by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.exception;

public final class TrackPointWrongOrderException extends RuntimeException {

    @SuppressWarnings("unused")
    public TrackPointWrongOrderException() {

    }

    public TrackPointWrongOrderException(final String description) {
        super(description);
    }

    @SuppressWarnings("unused")
    public TrackPointWrongOrderException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public TrackPointWrongOrderException(final String description, final Exception cause) {
        super(description, cause);
    }
}
