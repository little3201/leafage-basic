package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

/**
 * DTO class for User Role
 *
 * @author liwenqiang 2021-06-01 07:29
 */
public class UserRoleDTO implements Serializable {

    private static final long serialVersionUID = -2297833257581667657L;

    /**
     * 账号
     */
    @NotBlank
    private String username;
    /**
     * 角色
     */
    @NotEmpty
    private Set<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
