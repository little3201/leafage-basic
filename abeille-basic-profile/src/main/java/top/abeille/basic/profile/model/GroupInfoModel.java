package top.abeille.basic.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Model class for GroupInfo
 */
@Entity
@Table(name = "group_info")
public class GroupInfoModel {

    /**
     * 主键
     */
    private Long id;
    /**
     * 组织ID
     */
    private String groupId;
    /**
     * 组织代码
     */
    private String groupCode;
    /**
     * 组织领导ID
     */
    private Long groupLeaderId;
    /**
     * 上级组织ID
     */
    private Long groupUpperId;
    /**
     * 组织中文全称
     */
    private String groupFullNameCn;
    /**
     * 组织中文简称
     */
    private String groupSimpleNameCn;
    /**
     * 组织英文全称
     */
    private String groupFullNameEn;
    /**
     * 组织英文简称
     */
    private String groupSimpleNameEn;
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

    @Column(name = "group_id")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Column(name = "group_code")
    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Column(name = "group_leader_id")
    public Long getGroupLeaderId() {
        return groupLeaderId;
    }

    public void setGroupLeaderId(Long groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
    }

    @Column(name = "group_upper_id")
    public Long getGroupUpperId() {
        return groupUpperId;
    }

    public void setGroupUpperId(Long groupUpperId) {
        this.groupUpperId = groupUpperId;
    }

    @Column(name = "group_full_name_cn")
    public String getGroupFullNameCn() {
        return groupFullNameCn;
    }

    public void setGroupFullNameCn(String groupFullNameCn) {
        this.groupFullNameCn = groupFullNameCn;
    }

    @Column(name = "group_simple_name_cn")
    public String getgroupSimpleNameCn() {
        return groupSimpleNameCn;
    }

    public void setGroupSimpleNameCn(String groupSimpleNameCn) {
        this.groupSimpleNameCn = groupSimpleNameCn;
    }

    @Column(name = "group_full_name_en")
    public String getGroupFullNameEn() {
        return groupFullNameEn;
    }

    public void setGroupFullNameEn(String groupFullNameEn) {
        this.groupFullNameEn = groupFullNameEn;
    }

    @Column(name = "group_simple_name_en")
    public String getGroupSimpleNameEn() {
        return groupSimpleNameEn;
    }

    public void setGroupSimpleNameEn(String groupSimpleNameEn) {
        this.groupSimpleNameEn = groupSimpleNameEn;
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
