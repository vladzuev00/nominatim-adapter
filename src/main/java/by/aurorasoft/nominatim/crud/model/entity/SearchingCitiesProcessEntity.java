package by.aurorasoft.nominatim.crud.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SearchingCitiesProcessEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "search_step")
    private double searchStep;

    @Column(name = "total_points")
    private int totalPoints;

    @Column(name = "handled_points")
    private int handledPoints;

    @Column(name = "status")
    private Status status;

    public enum Status {
        HANDLING, SUCCESS, ERROR
    }
}
