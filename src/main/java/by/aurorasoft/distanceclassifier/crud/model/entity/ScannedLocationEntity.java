package by.aurorasoft.distanceclassifier.crud.model.entity;

import lombok.*;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "scanned_location")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ScannedLocationEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "geometry")
    private Geometry geometry;
}
