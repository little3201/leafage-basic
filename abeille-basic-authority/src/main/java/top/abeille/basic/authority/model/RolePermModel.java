package top.abeille.basic.authority.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Model class for RolePerm
 */
@Entity
@Table(name = "role_perm")
public class RolePermModel {

    /**
     * 主键
     */
    private Long id;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 权限ID
     */
    private Long permId;
    /**
     * 是否有效
     */
    @JsonIgnore
    @NotNull
    private Boolean valid;
    /**
     * 修改人ID
     */
    @JsonIgnore
    @NotNull
    private Long modifierId;
    /**
     * 修改时间
     */
    @JsonIgnore
    @NotNull
    private Date modifyTime;

    /**
     * Get 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "role_id")
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Column(name = "perm_id")
    public Long getPermId() {
        return permId;
    }

    public void setPermId(Long permId) {
        this.permId = permId;
    }

    @Column(name = "is_valid")
    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @Column(name = "modifier_id")
    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    @Column(name = "modify_time")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
