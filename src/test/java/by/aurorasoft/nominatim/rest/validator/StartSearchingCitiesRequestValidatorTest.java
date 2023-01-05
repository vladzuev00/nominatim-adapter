package by.aurorasoft.nominatim.rest.validator;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.rest.controller.exception.ConstraintException;
import by.aurorasoft.nominatim.rest.model.StartSearchingCitiesRequest;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class StartSearchingCitiesRequestValidatorTest {
    private final StartSearchingCitiesRequestValidator validator;

    public StartSearchingCitiesRequestValidatorTest() {
        this.validator = new StartSearchingCitiesRequestValidator();
    }

    @Test
    public void validationShouldBeSuccessful() {
        final Errors givenErrors = mock(Errors.class);
        when(givenErrors.hasErrors()).thenReturn(false);

        final StartSearchingCitiesRequest givenRequest = StartSearchingCitiesRequest.builder()
                .bbox(
                        new AreaCoordinate(
                                new Coordinate(52.959981, 25.903515),
                                new Coordinate(52.959982, 25.903516)
                        ))
                .searchStep(0.01)
                .build();
        this.validator.validate(givenRequest, givenErrors);
    }

    @Test
    public void validationShouldNotBeSuccessfulBecauseOfExistingErrors() {
        final Errors givenErrors = mock(Errors.class);
        when(givenErrors.hasErrors()).thenReturn(true);

        final StartSearchingCitiesRequest givenRequest = StartSearchingCitiesRequest.builder()
                .bbox(
                        new AreaCoordinate(
                                new Coordinate(52.959981, 25.903515),
                                new Coordinate(52.959982, 25.903516)
                        ))
                .searchStep(0.01)
                .build();

        ConstraintException expectedException = null;
        try {
            this.validator.validate(givenRequest, givenErrors);
        } catch (final ConstraintException exception) {
            expectedException = exception;
        }

        assertNotNull(expectedException);
        assertSame(expectedException.findErrors().orElseThrow(), givenErrors);
    }

    @Test
    public void validationShouldNotBeSuccessfulBecauseOfNotValidAreaCoordinate() {
        final Errors givenErrors = mock(Errors.class);
        when(givenErrors.hasErrors()).thenReturn(false);

        final StartSearchingCitiesRequest givenRequest = StartSearchingCitiesRequest.builder()
                .bbox(
                        new AreaCoordinate(
                                new Coordinate(52.959981, 25.903515),
                                new Coordinate(52.959980, 25.903516)
                        ))
                .searchStep(0.01)
                .build();

        ConstraintException expectedException = null;
        try {
            this.validator.validate(givenRequest, givenErrors);
        } catch (final ConstraintException exception) {
            expectedException = exception;
        }

        assertNotNull(expectedException);
        assertNotNull(expectedException.getMessage());
    }
}
