package by.aurorasoft.nominatim.crud.model.entity;

import lombok.*;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "city")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CityEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "geometry")
    private Geometry geometry;
}
