package top.abeille.basic.data.model;

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

    //主键
    private Long id;
    //角色ID
    private Long roleId;
    //权限ID
    private Long permId;
    //是否有效
    @JsonIgnore
    @NotNull
    private Boolean valid;
    //修改人ID
    @JsonIgnore
    @NotNull
    private Long modifierId;
    //修改时间
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

    /**
     * Set 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get 角色ID
     */
    @Column(name = "role_id")
    public Long getRoleId() {
        return roleId;
    }

    /**
     * Set 角色ID
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * Get 权限ID
     */
    @Column(name = "perm_id")
    public Long getPermId() {
        return permId;
    }

    /**
     * Set 权限ID
     */
    public void setPermId(Long permId) {
        this.permId = permId;
    }

    /**
     * Get 是否有效
     */
    @Column(name = "is_valid")
    public Boolean getValid() {
        return valid;
    }

    /**
     * Set 是否有效
     */
    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    /**
     * Get 修改人ID
     */
    @Column(name = "modifier_id")
    public Long getModifierId() {
        return modifierId;
    }

    /**
     * Set 修改人ID
     */
    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    /**
     * Get 修改时间
     */
    @Column(name = "modify_time")
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * Set 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
