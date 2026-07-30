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
package top.leafage.hypervisor.system.domain;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.leafage.common.data.jpa.domain.JpaAbstractAuditable;

import java.util.HashSet;
import java.util.Set;

/**
 * entity class for group.
 *
 * @author wq li
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "groups")
public class Group extends JpaAbstractAuditable<@NonNull String, @NonNull Long> {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    private final Set<User> members = new HashSet<>();

    /**
     * roles
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "group_roles",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private final Set<Role> roles = new HashSet<>();

    /**
     * group privileges
     */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<GroupPrivilege> groupPrivileges = new HashSet<>();

    /**
     * group authorities
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "group_authorities", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "authority")
    private final Set<String> authorities = new HashSet<>();

    @Column(name = "group_name", unique = true, nullable = false)
    private String name;

    private Long superiorId;

    private boolean enabled = true;


    public Group() {
    }

    public Group(String name, Long superiorId) {
        this.name = name;
        this.superiorId = superiorId;
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void removeMember(User user) {
        this.members.remove(user);
    }

    public void addRole(Role role) {
        if (this.roles.add(role)) {
            syncAuthorities();
        }
    }

    public void removeRole(Role role) {
        if (this.roles.remove(role)) {
            syncAuthorities();
        }
    }

    public void addPrivilege(Privilege privilege, Set<String> actions) {
        GroupPrivilege gp = new GroupPrivilege();
        gp.setGroup(this);
        gp.setPrivilege(privilege);
        gp.addActions(actions);
        if (this.groupPrivileges.add(gp)) {
            syncAuthorities();
        }
    }

    public void removePrivilege(Privilege privilege) {
        this.groupPrivileges.removeIf(gp -> gp.getPrivilege().equals(privilege));
        syncAuthorities();
    }

    public void removePrivilegeAction(Privilege privilege, String action) {
        this.groupPrivileges.stream()
                .filter(gp -> gp.getPrivilege().equals(privilege))
                .findFirst()
                .ifPresent(gp -> {
                    gp.removeAction(action);
                    if (gp.hasNoActions()) {
                        this.groupPrivileges.remove(gp);
                    }
                });
        syncAuthorities();
    }

    /**
     * 核心同步方法：把 Role 的权限 + Group 自身的权限全部转换成 authorities
     */
    public void syncAuthorities() {
        this.authorities.clear();

        // 关联的 Roles
        for (Role role : roles) {
            if (role.isBuiltIn()) {
                authorities.add(
                        "ROLE_" + role.getCode()
                );
            }

            for (RolePrivilege rp : role.getRolePrivileges()) {
                addAuthorities(rp.getPrivilege().getName(), rp.getActions());
            }
        }

        // 直接配置的 Privileges
        for (GroupPrivilege gp : groupPrivileges) {
            addAuthorities(gp.getPrivilege().getName(), gp.getActions());
        }
    }

    /**
     * Add authority for security. Example：users:create
     *
     * @param privilegeName the privilege name.
     * @param actions       the action under the privilege.
     */
    private void addAuthorities(String privilegeName, Set<String> actions) {
        this.authorities.add(privilegeName);
        for (String action : actions) {
            String authority = privilegeName + ":" + action;
            this.authorities.add(authority);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
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

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }

    public Set<GroupPrivilege> getGroupPrivileges() {
        return Set.copyOf(groupPrivileges);
    }

    public Set<String> getAuthorities() {
        return Set.copyOf(authorities);
    }
}
