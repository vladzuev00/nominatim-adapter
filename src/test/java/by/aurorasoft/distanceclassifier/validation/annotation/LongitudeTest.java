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

public final class LongitudeTest extends AbstractSpringBootTest {

    @Autowired
    private Validator validator;

    @Test
    public void objectShouldBeValid() {
        final TestObject givenObject = new TestObject(40);

        final Set<ConstraintViolation<TestObject>> violations = validator.validate(givenObject);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void objectShouldNotBeValidBecauseOfLongitudeIsLessThanMinimalAllowable() {
        final TestObject givenObject = new TestObject(-180.001);

        final Set<ConstraintViolation<TestObject>> violations = validator.validate(givenObject);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Test
    public void objectShouldNotBeValidBecauseOfLongitudeIsMoreThanMaximalAllowable() {
        final TestObject givenObject = new TestObject(180.001);

        final Set<ConstraintViolation<TestObject>> violations = validator.validate(givenObject);
        assertEquals(1, violations.size());
        assertEquals("Invalid longitude", findFirstMessage(violations));
    }

    @Value
    private static class TestObject {

        @Longitude
        double longitude;
    }
}
