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

package top.leafage.hypervisor.messages.domain;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.leafage.common.data.jpa.domain.JpaAbstractAuditable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * entity class for message.
 *
 * @author wq li
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "messages")
public class Message extends JpaAbstractAuditable<@NonNull String, @NonNull Long> {

    private String title;

    private String body;

    private String type;

    @Enumerated(EnumType.STRING)
    private Scope scope;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DRAFT;

    @OneToMany(
            mappedBy = "message",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<MessageTarget> targets = new HashSet<>();

    private LocalDateTime publishedAt;

    public Message() {
    }

    public Message(String title, String body, String type, Scope scope) {
        this.title = title;
        this.body = body;
        this.type = type;
        this.scope = scope;
    }

    public enum Scope {
        ALL,
        USER,
        GROUP,
        ROLE;
    }

    public enum Status {
        DRAFT,
        PUBLISHED,
        REVOKED;
    }

    public void addTarget(MessageTarget.TargetType type, Long targetId) {
        MessageTarget target = new MessageTarget(
                this,
                type,
                targetId
        );

        targets.add(target);
    }


    public void clearTargets() {
        targets.clear();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Set<MessageTarget> getTargets() {
        return targets;
    }

    public void setTargets(Set<MessageTarget> targets) {
        this.targets = targets;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}
