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
package io.leafage.basic.assets.audit;

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
 * @author wq li  2020-12-20 9:54
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


    @Override
    public Optional<String> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    @Override
    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable(this.createdDate);
    }

    @Override
    public void setCreatedDate(Instant creationDate) {
        this.createdDate = creationDate;
    }


    @Override
    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }


    @Override
    public Optional<Instant> getLastModifiedDate() {
        return Optional.ofNullable(this.lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(Instant lastModifiedDate) {
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
