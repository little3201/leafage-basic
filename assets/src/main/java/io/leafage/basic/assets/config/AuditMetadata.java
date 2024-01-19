/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.config;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * audit metadata.
 *
 * @author liwenqiang  2020-12-20 9:54
 */
@MappedSuperclass
public abstract class AuditMetadata implements Auditable<String, Long, Instant> {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 创建人
     */
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    /**
     * 最后修改人
     */
    @Column(name = "last_modified_by", insertable = false)
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_date", insertable = false)
    private Instant lastModifiedDate;


    @Nonnull
    @Override
    public Optional<String> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    @Override
    public void setCreatedBy(@Nonnull String createdBy) {
        this.createdBy = createdBy;
    }

    @Nonnull
    @Override
    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable(this.createdDate);
    }

    @Override
    public void setCreatedDate(@Nonnull Instant creationDate) {
        this.createdDate = creationDate;
    }

    @Nonnull
    @Override
    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(@Nonnull String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Nonnull
    @Override
    public Optional<Instant> getLastModifiedDate() {
        return Optional.ofNullable(this.lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(@Nonnull Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(getId());
    }
}
