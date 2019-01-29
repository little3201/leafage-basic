package top.abeille.basic.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import top.abeille.basic.data.view.UserView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Model class for UserInfo
 */
@Entity
@Table(name = "user_info")
public class UserInfoModel {

    private Long id;
    //用户ID
    private String userId;
    //用户角色主键
    private Long userRoleId;
    //组织主键
    private Long groupId;
    //中文姓名
    @JsonView(UserView.Summary.class)
    private String userNameCn;
    //英文姓名
    @JsonView(UserView.Summary.class)
    private String userNameEn;
    //用户名
    @JsonView(UserView.Summary.class)
    private String username;
    //密码
    @JsonIgnore
    private String password;
    //电话
    @JsonView(UserView.Summary.class)
    private String userMobile;
    //邮箱
    @JsonView(UserView.Summary.class)
    private String userEmail;
    //地址
    @JsonView(UserView.Summary.class)
    private String userAddress;
    //是否有效
    @JsonIgnore
    private Boolean accountNonExpired;
    //是否没有锁定
    @JsonIgnore
    private Boolean accountNonLocked;
    //密码是否有效
    @JsonIgnore
    private Boolean credentialsNonExpired;
    //是否激活
    @JsonIgnore
    private Boolean enabled;
    //修改人ID
    @NotNull
    @JsonIgnore
    private Long modifierId;
    //修改时间
    @NotNull
    @JsonIgnore
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
     * Get 用户ID
     */
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    /**
     * Set 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get 用户角色主键
     */
    @Column(name = "user_role_id")
    public Long getUserRoleId() {
        return userRoleId;
    }

    /**
     * Set 用户角色主键
     */
    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    /**
     * Get 组织主键
     */
    @Column(name = "group_id")
    public Long getGroupId() {
        return groupId;
    }

    /**
     * Set 组织主键
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * Get 中文姓名
     */
    @Column(name = "user_name_cn")
    public String getUserNameCn() {
        return userNameCn;
    }

    /**
     * Set 中文姓名
     */
    public void setUserNameCn(String userNameCn) {
        this.userNameCn = userNameCn;
    }

    /**
     * Get 英文姓名
     */
    @Column(name = "user_name_en")
    public String getUserNameEn() {
        return userNameEn;
    }

    /**
     * Set 英文姓名
     */
    public void setUserNameEn(String userNameEn) {
        this.userNameEn = userNameEn;
    }

    /**
     * Get 用户名-账号
     */
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    /**
     * Set 用户名-账号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get 密码
     */
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    /**
     * Set 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get 电话
     */
    @Column(name = "user_mobile")
    public String getUserMobile() {
        return userMobile;
    }

    /**
     * Set 电话
     */
    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    /**
     * Get 邮箱
     */
    @Column(name = "user_email")
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Set 邮箱
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Get 地址
     */
    @Column(name = "user_address")
    public String getUserAddress() {
        return userAddress;
    }

    /**
     * Set 地址
     */
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /**
     * Get 是否未失效
     */
    @Column(name = "is_account_non_expired")
    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * Set 是否未失效
     */
    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     * Get 是否未锁定
     */
    @Column(name = "is_account_non_locked")
    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * Set 是否未锁定
     */
    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * Get 是否资格未失效
     */
    @Column(name = "is_credentials_non_expired")
    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * Set 是否资格未失效
     */
    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * Get 是否激活
     */
    @Column(name = "is_enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Set 是否激活
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
