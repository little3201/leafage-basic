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

import org.springframework.data.domain.Auditable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Optional;

/**
 * model class for access log
 *
 * @author liwenqiang 2022-03-18 21:09
 */
@Table(name = "access_logs")
public class AccessLog implements Auditable<String, Long, Instant> {

    /**
     * ip
     */
    private String ip;
    /**
     * location
     */
    private String location;
    /**
     * context
     */
    private String context;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public Optional<String> getCreatedBy() {
        return Optional.empty();
    }

    @Override
    public void setCreatedBy(String createdBy) {

    }

    @Override
    public Optional<Instant> getCreatedDate() {
        return Optional.empty();
    }

    @Override
    public void setCreatedDate(Instant creationDate) {

    }

    @Override
    public Optional<String> getLastModifiedBy() {
        return Optional.empty();
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {

    }

    @Override
    public Optional<Instant> getLastModifiedDate() {
        return Optional.empty();
    }

    @Override
    public void setLastModifiedDate(Instant lastModifiedDate) {

    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
