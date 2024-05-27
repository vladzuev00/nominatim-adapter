package by.aurorasoft.distanceclassifier.config.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
@Validated
@ConstructorBinding
@ConfigurationProperties("overpass")
public final class OverpassProperty {

    @NotNull
    @Positive
    private final Integer timeout;
}
