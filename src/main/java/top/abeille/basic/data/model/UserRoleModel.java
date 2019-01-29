package top.abeille.basic.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Model class for UserRole
 *
 * @author liwenqiang 2018/12/4 10:09
 **/
@Entity
@Table(name = "user_role")
public class UserRoleModel {

    //主键
    private Long id;
    //用户主键
    private Long userId;
    //角色主键
    private Long roleId;
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
     * Get 用户主键
     */
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    /**
     * Set 用户主键
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Get 角色主键
     */
    @Column(name = "role_id")
    public Long getRoleId() {
        return roleId;
    }

    /**
     * Set 角色主键
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
