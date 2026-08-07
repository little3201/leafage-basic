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
 * entity class for user.
 *
 * @author wq li
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User extends JpaAbstractAuditable<@NonNull String, @NonNull Long> {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    private String email;

    private boolean enabled = true;

    /**
     * roles
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private final Set<Role> roles = new HashSet<>();

    /**
     * authorities
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    @Column(name = "authority")
    private final Set<String> authorities = new HashSet<>();


    public User() {
    }

    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
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

    /**
     * 核心同步方法：把 Role 的权限转换成 authorities
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }

    public Set<String> getAuthorities() {
        return Set.copyOf(authorities);
    }
}
