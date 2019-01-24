package top.abeille.basic.authority.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Model class for PermInfo
 */
@Entity
@Table(name = "perm_info")
public class PermInfoModel {

    //主键
    private Long id;
    //权限中文名称
    private String permNameCn;
    //权限英文名称
    private String permNameEn;
    //权限路径
    private String permPath;
    //权限描述
    private String permDesc;
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
    public void setId(Long permId) {
        this.id = id;
    }

    /**
     * Get 权限中文名称
     */
    @Column(name = "perm_name_cn")
    public String getPermNameCn() {
        return permNameCn;
    }

    /**
     * Set 权限中文名称
     */
    public void setPermNameCn(String permNameCn) {
        this.permNameCn = permNameCn;
    }

    /**
     * Get 权限英文名称
     */
    @Column(name = "perm_name_en")
    public String getPermNameEn() {
        return permNameEn;
    }

    /**
     * Set 权限英文名称
     */
    public void setPermNameEn(String permNameEn) {
        this.permNameEn = permNameEn;
    }

    /**
     * Get 权限路径
     */
    @Column(name = "perm_path")
    public String getPermPath() {
        return permPath;
    }

    /**
     * Set 权限路径
     */
    public void setPermPath(String permPath) {
        this.permPath = permPath;
    }

    /**
     * Get 权限描述
     */
    @Column(name = "perm_desc")
    public String getPermDesc() {
        return permDesc;
    }

    /**
     * Set 权限描述
     */
    public void setPermDesc(String permDesc) {
        this.permDesc = permDesc;
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
