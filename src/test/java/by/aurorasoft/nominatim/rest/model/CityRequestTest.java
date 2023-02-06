package by.aurorasoft.nominatim.rest.model;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.geojson.Geometry;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

import static by.aurorasoft.nominatim.crud.model.entity.CityEntity.Type.CAPITAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class CityRequestTest extends AbstractContextTest {

    @Autowired
    private Validator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void requestShouldBeValid() {
        final CityRequest givenRequest = CityRequest.builder()
                .name("Minsk-Minsk")
                .geometry(new Geometry() {
                })
                .type(CAPITAL)
                .build();
        final Set<ConstraintViolation<CityRequest>> constraintViolations = this.validator.validate(givenRequest);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfNameIsNull() {
        final CityRequest givenRequest = CityRequest.builder()
                .geometry(new Geometry() {
                })
                .type(CAPITAL)
                .build();
        final Set<ConstraintViolation<CityRequest>> constraintViolations = this.validator.validate(givenRequest);
        assertEquals(1, constraintViolations.size());
        assertEquals("не должно равняться null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfNameIsNotValid() {
        final CityRequest givenRequest = CityRequest.builder()
                .name(" Minsk")
                .geometry(new Geometry() {
                })
                .type(CAPITAL)
                .build();
        final Set<ConstraintViolation<CityRequest>> constraintViolations = this.validator.validate(givenRequest);
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "должно соответствовать \"^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$\"",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfGeometryIsNull() {
        final CityRequest givenRequest = CityRequest.builder()
                .name("Minsk")
                .type(CAPITAL)
                .build();
        final Set<ConstraintViolation<CityRequest>> constraintViolations = this.validator.validate(givenRequest);
        assertEquals(1, constraintViolations.size());
        assertEquals("не должно равняться null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfTypeIsNull() {
        final CityRequest givenRequest = CityRequest.builder()
                .name("Minsk-Minsk")
                .geometry(new Geometry() {
                })
                .build();
        final Set<ConstraintViolation<CityRequest>> constraintViolations = this.validator.validate(givenRequest);
        assertEquals(1, constraintViolations.size());
        assertEquals("не должно равняться null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void requestShouldBeConvertedToJson()
            throws JsonProcessingException {
        final CityRequest givenRequest = CityRequest.builder()
                .name("Minsk-Minsk")
                .geometry(new Geometry() {
                })
                .type(CAPITAL)
                .build();
        final String actual = this.objectMapper.writeValueAsString(givenRequest);
        final String expected = "{\"name\":\"Minsk-Minsk\",\"geometry\":{\"type\":\"\"},\"type\":\"CAPITAL\"}";
        assertEquals(expected, actual);
    }

    @Test
    public void jsonShouldBeConvertedToRequest()
            throws JsonProcessingException {
        final String givenJson = "{\"name\":\"Minsk-Minsk\",\"geometry\":{\"type\":\"\"},\"type\":\"CAPITAL\"}";
        final CityRequest actual = this.objectMapper.readValue(givenJson, CityRequest.class);
        final CityRequest expected = CityRequest.builder()
                .name("Minsk-Minsk")
                .geometry(new Geometry() {
                })
                .type(CAPITAL)
                .build();
        assertEquals(expected, actual);
    }
}
