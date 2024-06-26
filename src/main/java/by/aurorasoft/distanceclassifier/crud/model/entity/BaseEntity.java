package by.aurorasoft.distanceclassifier.crud.model.entity;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import org.hibernate.Hibernate;

import java.util.Objects;

import static java.util.Objects.hash;

public abstract class BaseEntity<IdType> implements AbstractEntity<IdType> {
    public abstract IdType getId();

    @Override
    @SuppressWarnings({"unchecked", "EqualsWhichDoesntCheckParameterClass"})
    public final boolean equals(final Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (Hibernate.getClass(this) != Hibernate.getClass(otherObject)) {
            return false;
        }
        final BaseEntity<IdType> other = (BaseEntity<IdType>) otherObject;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return hash(getId());
    }
}
