package by.aurorasoft.nominatim.crud.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.TypeDef;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;

import java.util.Objects;

import static java.util.Arrays.stream;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "city")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@SequenceGenerator(name = "city_id_seq", sequenceName = "city_id_seq", allocationSize = 1)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class CityEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "city_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "geometry")
    private Geometry geometry;

    @Enumerated(STRING)
    @Column(name = "type")
    @org.hibernate.annotations.Type(type = "pgsql_enum")
    private Type type;

    @Column(name = "bounding_box")
    private Geometry boundingBox;

    public enum Type {
        CAPITAL("yes"), REGIONAL("4"), NOT_DEFINED(null);

        private final String capitalJsonValue;

        Type(String capitalJsonValue) {
            this.capitalJsonValue = capitalJsonValue;
        }

        public static Type findByCapitalJsonValue(String capitalJsonValue) {
            return stream(Type.values())
                    .filter(type -> Objects.equals(capitalJsonValue, type.capitalJsonValue))
                    .findAny()
                    .orElse(NOT_DEFINED);
        }
    }
}
