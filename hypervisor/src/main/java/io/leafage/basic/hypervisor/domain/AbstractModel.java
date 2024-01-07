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

package io.leafage.basic.hypervisor.domain;

import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

/**
 * base model
 *
 * @author liwenqiang 2024-01-04 20:27
 */
public abstract class AbstractModel {

    /**
     * 创建人
     */
    @Column(value = "created_by")
    private String createdBy;
    /**
     * 创建时间
     */
    @Column(value = "created_at")
    private LocalDateTime createdAt;
    /**
     * 最后修改人
     */
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    /**
     * 最后修改时间
     */
    @Column(value = "last_updated_at")
    private LocalDateTime lastUpdatedAt;


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
