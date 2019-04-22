package top.abeille.basic.authority.model;

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

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户主键
     */
    private Long userId;
    /**
     * 角色主键
     */
    private Long roleId;
    /**
     * 是否有效
     */
    @JsonIgnore
    @NotNull
    private Boolean valid;
    /**
     * 修改人ID
     */
    @NotNull
    @JsonIgnore
    private Long modifierId;
    /**
     * 修改时间
     */
    @NotNull
    @JsonIgnore
    private Date modifyTime;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "role_id")
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
