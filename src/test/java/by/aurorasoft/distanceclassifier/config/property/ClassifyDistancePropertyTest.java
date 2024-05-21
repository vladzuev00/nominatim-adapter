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

public final class ClassifyDistancePropertyTest extends AbstractSpringBootTest {

    @Autowired
    private ClassifyDistanceProperty property;

    @Autowired
    private Validator validator;

    @Test
    public void propertyShouldBeCreated() {
        final ClassifyDistanceProperty expected = new ClassifyDistanceProperty(false, 0.00015, 50.);
        assertEquals(expected, property);
    }

    @Test
    public void propertyShouldBeValid() {
        final ClassifyDistanceProperty givenProperty = new ClassifyDistanceProperty(false, 0.00015, 500.);

        final Set<ConstraintViolation<ClassifyDistanceProperty>> violations = validator.validate(givenProperty);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfCacheGeometriesIsNull() {
        final ClassifyDistanceProperty givenProperty = ClassifyDistanceProperty.builder()
                .trackSimplifyEpsilon(0.00015)
                .pointMinGpsRelative(500.)
                .build();

        final Set<ConstraintViolation<ClassifyDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfTrackSimplifyEpsilonIsNull() {
        final ClassifyDistanceProperty givenProperty = ClassifyDistanceProperty.builder()
                .cacheGeometries(true)
                .pointMinGpsRelative(500.)
                .build();

        final Set<ConstraintViolation<ClassifyDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfTrackSimplifyEpsilonIsLessThanMinimalAllowable() {
        final ClassifyDistanceProperty givenProperty = new ClassifyDistanceProperty(true, -0.001, 500.);

        final Set<ConstraintViolation<ClassifyDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше, чем или равно 0", findFirstMessage(violations));
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfPointMinGpsRelativeIsNull() {
        final ClassifyDistanceProperty givenProperty = ClassifyDistanceProperty.builder()
                .cacheGeometries(true)
                .trackSimplifyEpsilon(0.001)
                .build();

        final Set<ConstraintViolation<ClassifyDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfPointMinGpsRelativeIsLessThanMinimalAllowable() {
        final ClassifyDistanceProperty givenProperty = new ClassifyDistanceProperty(false, 0.00015, -0.001);

        final Set<ConstraintViolation<ClassifyDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше, чем или равно 0", findFirstMessage(violations));
    }
}
