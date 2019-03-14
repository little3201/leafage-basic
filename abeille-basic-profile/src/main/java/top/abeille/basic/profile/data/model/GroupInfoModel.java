package top.abeille.basic.profile.data.model;

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

    //主键
    private Long id;
    //组织ID
    private String groupId;
    //组织代码
    private String groupCode;
    //组织领导ID
    private Long groupLeaderId;
    //上级组织ID
    private Long groupUpperId;
    //组织中文全称
    private String groupFullNameCn;
    //组织中文简称
    private String groupSimpleNameCn;
    //组织英文全称
    private String groupFullNameEn;
    //组织英文简称
    private String groupSimpleNameEn;
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
     * Get 组织ID
     */
    @Column(name = "group_id")
    public String getGroupId() {
        return groupId;
    }

    /**
     * Set 组织ID
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * Get 组织代码
     */
    @Column(name = "group_code")
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * Set 组织代码
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * Get 组织领导ID
     */
    @Column(name = "group_leader_id")
    public Long getGroupLeaderId() {
        return groupLeaderId;
    }

    /**
     * Set 组织领导ID
     */
    public void setGroupLeaderId(Long groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
    }

    /**
     * Get 上级组织ID
     */
    @Column(name = "group_upper_id")
    public Long getGroupUpperId() {
        return groupUpperId;
    }

    /**
     * Set 上级组织ID
     */
    public void setGroupUpperId(Long groupUpperId) {
        this.groupUpperId = groupUpperId;
    }

    /**
     * Get 组织中文全称
     */
    @Column(name = "group_full_name_cn")
    public String getGroupFullNameCn() {
        return groupFullNameCn;
    }

    /**
     * Set 组织中文全称
     */
    public void setGroupFullNameCn(String groupFullNameCn) {
        this.groupFullNameCn = groupFullNameCn;
    }

    /**
     * Get 组织中文简称
     */
    @Column(name = "group_simple_name_cn")
    public String getgroupSimpleNameCn() {
        return groupSimpleNameCn;
    }

    /**
     * Set 组织中文简称
     */
    public void setGroupSimpleNameCn(String groupSimpleNameCn) {
        this.groupSimpleNameCn = groupSimpleNameCn;
    }

    /**
     * Get 组织英文全称
     */
    @Column(name = "group_full_name_en")
    public String getGroupFullNameEn() {
        return groupFullNameEn;
    }

    /**
     * Set 组织英文全称
     */
    public void setGroupFullNameEn(String groupFullNameEn) {
        this.groupFullNameEn = groupFullNameEn;
    }

    /**
     * Get 组织英文简称
     */
    @Column(name = "group_simple_name_en")
    public String getGroupSimpleNameEn() {
        return groupSimpleNameEn;
    }

    /**
     * Set 组织英文简称
     */
    public void setGroupSimpleNameEn(String groupSimpleNameEn) {
        this.groupSimpleNameEn = groupSimpleNameEn;
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
