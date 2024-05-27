package by.aurorasoft.distanceclassifier.config.property;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static by.aurorasoft.distanceclassifier.testutil.ConstraintViolationUtil.findFirstMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class OverpassPropertyTest extends AbstractSpringBootTest {

    @Autowired
    private OverpassProperty property;

    @Autowired
    private Validator validator;

    @Test
    public void propertyShouldBeCreated() {
        final OverpassProperty expected = new OverpassProperty(50);
        assertEquals(expected, property);
    }

    @Test
    public void propertyShouldBeValid() {
        final OverpassProperty givenProperty = new OverpassProperty(1);

        final Set<ConstraintViolation<OverpassProperty>> violations = validator.validate(givenProperty);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfTimeoutIsNull() {
        final OverpassProperty givenProperty = OverpassProperty.builder().build();

        final Set<ConstraintViolation<OverpassProperty>> violations = validator.validate(givenProperty);
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfTimeoutIsNotPositive() {
        final OverpassProperty givenProperty = new OverpassProperty(0);

        final Set<ConstraintViolation<OverpassProperty>> violations = validator.validate(givenProperty);
        assertEquals("должно быть больше 0", findFirstMessage(violations));
    }
}
