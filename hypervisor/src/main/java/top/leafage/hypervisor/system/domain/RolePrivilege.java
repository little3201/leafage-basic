/*
 * Copyright (c) 2024-2025.  little3201.
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
package top.leafage.hypervisor.system.domain;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * entity class for role privileges.
 *
 * @author wq li
 */
@Entity
@Table(name = "role_privileges")
public class RolePrivilege extends AbstractPersistable<@NonNull Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_id", nullable = false)
    private Privilege privilege;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_privilege_actions", joinColumns = @JoinColumn(name = "role_privilege_id"))
    private final Set<String> actions = new HashSet<>();


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public Set<String> getActions() {
        return Set.copyOf(actions);
    }

    public void addActions(Collection<String> newActions) {
        if (newActions != null) {
            this.actions.addAll(newActions);
        }
    }
}
