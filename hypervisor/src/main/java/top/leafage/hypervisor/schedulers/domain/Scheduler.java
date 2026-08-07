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

package top.leafage.hypervisor.schedulers.domain;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.leafage.common.data.jpa.domain.JpaAbstractAuditable;

import java.time.Instant;

/**
 * entity class for scheduler.
 *
 * @author wq li
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "schedulers")
public class Scheduler extends JpaAbstractAuditable<@NonNull String, @NonNull Long> {

    private String name;

    private String cronExpression;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Instant lastExecuteTime;

    private Instant nextExecuteTime;

    @Column(columnDefinition = "text")
    private String record;

    private boolean enabled = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Scheduler() {
    }

    public Scheduler(String name, String cronExpression) {
        this.name = name;
        this.cronExpression = cronExpression;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        PENDING, RUNNING, SUCCEED, FAILED, CANCELED
    }

    public Instant getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Instant lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    public Instant getNextExecuteTime() {
        return nextExecuteTime;
    }

    public void setNextExecuteTime(Instant nextExecuteTime) {
        this.nextExecuteTime = nextExecuteTime;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
