/*
 *  Copyright 2018-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.leafage.basic.assets.config;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * audit metadata
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public abstract class AuditMetadata implements Auditable<String, Long, Instant> {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 创建人
     */
    @InsertOnlyProperty
    @Column(value = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @InsertOnlyProperty
    @CreatedDate
    @Column(value = "created_date")
    private Instant createdDate;

    /**
     * 最后修改人
     */
    @Column(value = "last_modified_by")
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(value = "last_modified_date")
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
