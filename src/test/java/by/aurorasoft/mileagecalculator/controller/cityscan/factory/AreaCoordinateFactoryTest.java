package by.aurorasoft.mileagecalculator.controller.cityscan.factory;

import by.aurorasoft.mileagecalculator.controller.cityscan.model.AreaCoordinateRequest;
import by.aurorasoft.mileagecalculator.model.AreaCoordinate;
import by.aurorasoft.mileagecalculator.model.Coordinate;
import by.aurorasoft.mileagecalculator.controller.exception.CustomValidationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class AreaCoordinateFactoryTest {
    private final AreaCoordinateFactory factory = new AreaCoordinateFactory();

    @Test
    public void coordinateShouldBeCreated() {
        final double givenMinLatitude = 5.5;
        final double givenMinLongitude = 6.6;
        final double givenMaxLatitude = 7.7;
        final double givenMaxLongitude = 8.8;
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(
                givenMinLatitude,
                givenMinLongitude,
                givenMaxLatitude,
                givenMaxLongitude
        );

        final AreaCoordinate actual = factory.create(givenRequest);
        final AreaCoordinate expected = new AreaCoordinate(
                new Coordinate(givenMinLatitude, givenMinLongitude),
                new Coordinate(givenMaxLatitude, givenMaxLongitude)
        );
        assertEquals(expected, actual);
    }

    @Test(expected = CustomValidationException.class)
    public void coordinateShouldNotBeCreatedBecauseOfNotValidAreaCoordinate() {
        final double givenMinLatitude = 9.9;
        final double givenMinLongitude = 6.6;
        final double givenMaxLatitude = 7.7;
        final double givenMaxLongitude = 8.8;
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(
                givenMinLatitude,
                givenMinLongitude,
                givenMaxLatitude,
                givenMaxLongitude
        );

        factory.create(givenRequest);
    }
}
