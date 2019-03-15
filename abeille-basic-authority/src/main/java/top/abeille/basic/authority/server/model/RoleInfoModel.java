package top.abeille.basic.authority.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Model class for RoleInfo
 */
@Entity
@Table(name = "role_info")
public class RoleInfoModel {

    //主键
    private Long id;
    //角色名称
    private String roleName;
    //角色描述
    private String roleDesc;
    //备注
    private String roleRemark;
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
     * Get 角色ID
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Set 角色ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get 角色名称
     */
    @Column(name = "role_name")
    public String getRoleName() {
        return roleName;
    }

    /**
     * Set 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Get 角色描述
     */
    @Column(name = "role_desc")
    public String getRoleDesc() {
        return roleDesc;
    }

    /**
     * Set 角色描述
     */
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    /**
     * Get 备注
     */
    @Column(name = "role_remark")
    public String getRoleRemark() {
        return roleRemark;
    }

    /**
     * Set 备注
     */
    public void setRoleRemark(String roleRemark) {
        this.roleRemark = roleRemark;
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
