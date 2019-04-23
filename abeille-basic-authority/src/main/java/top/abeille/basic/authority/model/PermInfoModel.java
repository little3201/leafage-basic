package top.abeille.basic.authority.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Model class for PermInfo
 */
@Entity
@Table(name = "perm_info")
public class PermInfoModel {

    /**
     * 主键
     */
    private Long id;
    /**
     * 权限中文名称
     */
    private String permNameCn;
    /**
     * 权限英文名称
     */
    private String permNameEn;
    /**
     * 权限路径
     */
    private String permPath;
    /**
     * 权限描述
     */
    private String permDesc;
    /**
     * 是否有效
     */
    @JsonIgnore
    private Boolean enabled;
    /**
     * 修改人ID
     */
    @JsonIgnore
    private Long modifierId;

    /**
     * 修改时间
     */
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

    @Column(name = "perm_name_cn")
    public String getPermNameCn() {
        return permNameCn;
    }

    public void setPermNameCn(String permNameCn) {
        this.permNameCn = permNameCn;
    }

    @Column(name = "perm_name_en")
    public String getPermNameEn() {
        return permNameEn;
    }

    public void setPermNameEn(String permNameEn) {
        this.permNameEn = permNameEn;
    }

    @Column(name = "perm_path")
    public String getPermPath() {
        return permPath;
    }

    public void setPermPath(String permPath) {
        this.permPath = permPath;
    }

    @Column(name = "perm_desc")
    public String getPermDesc() {
        return permDesc;
    }

    public void setPermDesc(String permDesc) {
        this.permDesc = permDesc;
    }

    @Column(name = "is_enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
