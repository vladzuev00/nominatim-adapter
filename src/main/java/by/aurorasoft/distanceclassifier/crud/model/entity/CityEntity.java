package by.aurorasoft.distanceclassifier.crud.model.entity;

import by.aurorasoft.distanceclassifier.model.CityType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "city")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class CityEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "city_id_seq")
    @SequenceGenerator(name = "city_id_seq", sequenceName = "city_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "geometry")
    private Geometry geometry;

    @Enumerated(STRING)
    @Column(name = "type")
    @Type(type = "pgsql_enum")
    private CityType type;

    @Column(name = "bounding_box")
    private Geometry boundingBox;
}
