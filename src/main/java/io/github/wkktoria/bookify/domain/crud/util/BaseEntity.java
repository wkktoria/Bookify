package io.github.wkktoria.bookify.domain.crud.util;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column(name = "uuid", nullable = false, unique = true)
    public UUID uuid = UUID.randomUUID();

    @CreationTimestamp
    @Column(name = "created_on")
    public Instant createdOn;

    @Version
    @Column(name = "version", nullable = false)
    public long version;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BaseEntity that = (BaseEntity) obj;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

}
