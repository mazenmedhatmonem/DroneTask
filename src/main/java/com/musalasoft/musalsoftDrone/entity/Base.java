package com.musalasoft.musalsoftDrone.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass()
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public abstract class Base implements Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @Column(name = "id", nullable = false, unique = true, updatable = false, insertable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Long id;

    @Getter
    @Basic
    @CreationTimestamp
    @Column()
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected java.time.LocalDateTime createdAt;

    @Getter
    @Basic
    @UpdateTimestamp
    @Column()
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected java.time.LocalDateTime updatedAt;

    public boolean equals(Object object) {

        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Base that = (Base) object;
        return Objects.equals(id, that.id);
    }

}
