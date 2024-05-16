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

public final class ClassifyingDistancePropertyTest extends AbstractSpringBootTest {

    @Autowired
    private ClassifyingDistanceProperty property;

    @Autowired
    private Validator validator;

    @Test
    public void propertyShouldBeCreated() {
        final ClassifyingDistanceProperty expected = new ClassifyingDistanceProperty(false, 0.00015, 500.);
        assertEquals(expected, property);
    }

    @Test
    public void propertyShouldBeValid() {
        final ClassifyingDistanceProperty givenProperty = new ClassifyingDistanceProperty(false, 0.00015, 500.);

        final Set<ConstraintViolation<ClassifyingDistanceProperty>> violations = validator.validate(givenProperty);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfCacheGeometriesIsNull() {
        final ClassifyingDistanceProperty givenProperty = ClassifyingDistanceProperty.builder()
                .trackSimplifyEpsilon(0.00015)
                .pointUnionGpsRelativeThreshold(500.)
                .build();

        final Set<ConstraintViolation<ClassifyingDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfTrackSimplifyEpsilonIsNull() {
        final ClassifyingDistanceProperty givenProperty = ClassifyingDistanceProperty.builder()
                .cacheGeometries(true)
                .pointUnionGpsRelativeThreshold(500.)
                .build();

        final Set<ConstraintViolation<ClassifyingDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfTrackSimplifyEpsilonIsLessThanMinimalAllowable() {
        final ClassifyingDistanceProperty givenProperty = new ClassifyingDistanceProperty(true, -0.001, 500.);

        final Set<ConstraintViolation<ClassifyingDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше, чем или равно 0", findFirstMessage(violations));
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfPointUnionGpsRelativeThresholdIsNull() {
        final ClassifyingDistanceProperty givenProperty = ClassifyingDistanceProperty.builder()
                .cacheGeometries(true)
                .trackSimplifyEpsilon(0.001)
                .build();

        final Set<ConstraintViolation<ClassifyingDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("не должно равняться null", findFirstMessage(violations));
    }

    @Test
    public void propertyShouldNotBeValidBecauseOfPointUnionGpsRelativeThresholdIsLessThanMinimalAllowable() {
        final ClassifyingDistanceProperty givenProperty = new ClassifyingDistanceProperty(false, 0.00015, -0.001);

        final Set<ConstraintViolation<ClassifyingDistanceProperty>> violations = validator.validate(givenProperty);
        assertEquals(1, violations.size());
        assertEquals("должно быть больше, чем или равно 0", findFirstMessage(violations));
    }
}
