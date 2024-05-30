package by.aurorasoft.distanceclassifier.validation.annotation;

import by.aurorasoft.distanceclassifier.base.AbstractSpringBootTest;
import lombok.Value;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static by.aurorasoft.distanceclassifier.testutil.ConstraintViolationUtil.findFirstMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class LatitudeTest extends AbstractSpringBootTest {

    @Autowired
    private Validator validator;

    @Test
    public void objectShouldBeValid() {
        final TestObject givenObject = new TestObject(40);

        final Set<ConstraintViolation<TestObject>> violations = validator.validate(givenObject);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void objectShouldNotBeValidBecauseOfLatitudeIsLessThanMinimalAllowable() {
        final TestObject givenObject = new TestObject(-90.1);

        final Set<ConstraintViolation<TestObject>> violations = validator.validate(givenObject);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Test
    public void objectShouldNotBeValidBecauseOfLongitudeIsMoreThanMaximalAllowable() {
        final TestObject givenObject = new TestObject(90.1);

        final Set<ConstraintViolation<TestObject>> violations = validator.validate(givenObject);
        assertEquals(1, violations.size());
        assertEquals("Invalid latitude", findFirstMessage(violations));
    }

    @Value
    private static class TestObject {

        @Latitude
        double latitude;
    }
}
