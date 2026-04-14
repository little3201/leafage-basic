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
 * entity class for report sections.
 *
 * @author wq li
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorValue("report_sections")
public class ReportSection extends Section {

    private Long reportId;

    public ReportSection() {
    }

    public ReportSection(Long reportId, Section section) {
        super(section.getSuperiorId(), section.getName(), section.getLevel(), section.getBody(), section.getType().name());
        this.reportId = reportId;
    }

    public static ReportSection from(Long reportId, Section section) {
        return new ReportSection(reportId, section);
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

}
