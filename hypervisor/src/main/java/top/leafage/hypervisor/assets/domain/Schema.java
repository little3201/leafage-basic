/*
 * Copyright (c) 2026.  little3201.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.assets.domain;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.leafage.common.data.jpa.domain.JpaAbstractAuditable;

/**
 * entity class for schemas.
 *
 * @author wq li
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "schemas")
public class Schema extends JpaAbstractAuditable<@NonNull String, @NonNull Long> {

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Version
    private int version;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;

    private String description;

    private boolean enabled;


    public Schema() {
    }

    public Schema(String name, String type, String description) {
        this.name = name;
        this.type = Type.of(type);
        this.description = description;
    }

    public enum Status {
        DRAFT,
        PUBLISHED,
        ARCHIVED;

        public static Status of(String value) {
            return valueOf(value.toUpperCase());
        }
    }

    public enum Type {
        WORD,
        EXCEL;

        public static Type of(String value) {
            return valueOf(value.toUpperCase());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
