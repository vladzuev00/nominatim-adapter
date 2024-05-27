package by.aurorasoft.distanceclassifier.config.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
@Validated
@ConstructorBinding
@ConfigurationProperties("distance-classifying")
public final class ClassifyDistanceProperty {

    @NotNull
    private final Boolean cacheGeometries;

    @NotNull
    @DecimalMin("0")
    private final Double trackSimplifyEpsilon;

    @NotNull
    @DecimalMin("0")
    private final Double pointMinGpsRelative;
}
