package by.aurorasoft.nominatim.rest.validator;

import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import by.aurorasoft.nominatim.rest.controller.exception.ConstraintException;
import by.aurorasoft.nominatim.rest.model.StartSearchingCitiesRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static java.lang.Double.compare;

@Component
public final class StartSearchingCitiesRequestValidator {
    private static final String EXCEPTION_MESSAGE_NOT_VALID_AREA_COORDINATE
            = "Left bottom point's coordinates should be less than right upper point's coordinates.";

    public void validate(StartSearchingCitiesRequest requestBody, Errors errors) {
        if (errors.hasErrors()) {
            throw new ConstraintException(errors);
        }
        if (!isValidAreaCoordinate(requestBody.getBbox())) {
            throw new ConstraintException(EXCEPTION_MESSAGE_NOT_VALID_AREA_COORDINATE);
        }
    }

    private static boolean isValidAreaCoordinate(AreaCoordinate research) {
        final Coordinate leftBottom = research.getLeftBottom();
        final Coordinate rightUpper = research.getRightUpper();
        return compare(leftBottom.getLatitude(), rightUpper.getLatitude()) <= 0
                && compare(leftBottom.getLongitude(), rightUpper.getLongitude()) <= 0;
    }
}
