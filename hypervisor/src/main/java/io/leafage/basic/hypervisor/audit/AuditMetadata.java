/*
 *  Copyright 2018-2024 little3201.
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
package io.leafage.basic.hypervisor.audit;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * audit metadata.
 *
 * @author wq li
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditMetadata implements Auditable<String, Long, Instant> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private boolean enabled = true;

    /**
     * 创建人
     */
    @Column(name = "created_by", updatable = false, length = 50)
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
    @Column(name = "last_modified_by", insertable = false, length = 50)
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_date", insertable = false)
    private Instant lastModifiedDate;


    /**
     * <p>isEnabled.</p>
     *
     * @return a boolean
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * <p>Setter for the field <code>enabled</code>.</p>
     *
     * @param enabled a boolean
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable(this.createdDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCreatedDate(Instant creationDate) {
        this.createdDate = creationDate;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Instant> getLastModifiedDate() {
        return Optional.ofNullable(this.lastModifiedDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a {@link java.lang.Long} object
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNew() {
        return Objects.isNull(getId());
    }
}
