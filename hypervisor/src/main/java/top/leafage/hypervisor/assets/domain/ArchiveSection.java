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

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * entity class for archive sections.
 *
 * @author wq li
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorValue("archive")
public class ArchiveSection extends Section {

    private Long archiveId;

    public ArchiveSection() {
    }

    public ArchiveSection(Long archiveId, Section section) {
        super(section.getSuperiorId(), section.getName(), section.getSequence(), section.getLevel(), section.getBody(), section.getType().name());
        this.archiveId = archiveId;
    }

    public static ArchiveSection from(Long archiveId, Section section) {
        return new ArchiveSection(archiveId, section);
    }

    public Long getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(Long archiveId) {
        this.archiveId = archiveId;
    }
}
