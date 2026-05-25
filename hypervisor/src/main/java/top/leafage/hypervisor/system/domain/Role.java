/*
 * Copyright (c) 2024-2026.  little3201.
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.leafage.common.data.jpa.domain.JpaAbstractAuditable;

import java.util.HashSet;
import java.util.Set;

/**
 * entity class for role.
 *
 * @author wq li
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "roles")
public class Role extends JpaAbstractAuditable<@NonNull String, @NonNull Long> {

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_members",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    private final Set<User> members = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<RolePrivilege> rolePrivileges = new HashSet<>();

    public Role() {
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addPrivilege(Privilege privilege, Set<String> actions) {
        RolePrivilege rp = new RolePrivilege();
        rp.setRole(this);
        rp.setPrivilege(privilege);
        rp.addActions(actions);
        this.rolePrivileges.add(rp);
    }

    public void removePrivilege(Privilege privilege) {
        rolePrivileges.removeIf(rp -> rp.getPrivilege().equals(privilege));
    }

    public void removePrivilegeAction(Privilege privilege, String action) {
        rolePrivileges.stream()
                .filter(rp -> rp.getPrivilege().equals(privilege))
                .findFirst()
                .ifPresent(rp -> {
                    rp.removeAction(action);
                    if (rp.hasNoActions()) {
                        rolePrivileges.remove(rp);
                    }
                });
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void removeMember(User user) {
        this.members.remove(user);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<User> getMembers() {
        return Set.copyOf(members);
    }

    public Set<RolePrivilege> getRolePrivileges() {
        return Set.copyOf(rolePrivileges);
    }
}
