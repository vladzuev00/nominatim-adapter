package by.aurorasoft.distanceclassifier.validation.annotation;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
@DecimalMin("-180")
@DecimalMax("180")
public @interface Longitude {

}
