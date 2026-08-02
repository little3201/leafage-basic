/*
 * Copyright(c) 2019-present the original author or authors.
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

package top.leafage.hypervisor.schedulers.domain.dto;

import jakarta.validation.constraints.NotBlank;
import top.leafage.hypervisor.schedulers.domain.Scheduler;

/**
 * dto for scheduler.
 *
 * @author wq li
 */
public class SchedulerDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String cronExpression;

    public static Scheduler toEntity(SchedulerDTO dto) {
        return new Scheduler(
                dto.getName(),
                dto.getCronExpression()
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

}
