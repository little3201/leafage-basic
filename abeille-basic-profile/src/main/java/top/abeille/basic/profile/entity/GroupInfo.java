/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Model class for GroupInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "group_info")
public class GroupInfo {

    /**
     * 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    /**
     * 组织ID
     */
    @Column(name = "group_id")
    private String groupId;
    /**
     * 组织代码
     */
    @Column(name = "group_code")
    private String groupCode;
    /**
     * 组织领导ID
     */
    @Column(name = "group_leader_id")
    private Long groupLeaderId;
    /**
     * 上级组织ID
     */
    @Column(name = "group_upper_id")
    private Long groupUpperId;
    /**
     * 组织中文全称
     */
    @Column(name = "group_full_name_cn")
    private String groupFullNameCn;
    /**
     * 组织中文简称
     */
    @Column(name = "group_simple_name_cn")
    private String groupSimpleNameCn;
    /**
     * 组织英文全称
     */
    @Column(name = "group_full_name_en")
    private String groupFullNameEn;
    /**
     * 组织英文简称
     */
    @Column(name = "group_simple_name_en")
    private String groupSimpleNameEn;

    /**
     * 是否有效
     */
    @JsonIgnore
    @Column(name = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人ID
     */
    @JsonIgnore
    @Column(name = "modifier_id")
    private Long modifierId;
    /**
     * 修改时间
     */
    @JsonIgnore
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Long getGroupLeaderId() {
        return groupLeaderId;
    }

    public void setGroupLeaderId(Long groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
    }

    public Long getGroupUpperId() {
        return groupUpperId;
    }

    public void setGroupUpperId(Long groupUpperId) {
        this.groupUpperId = groupUpperId;
    }

    public String getGroupFullNameCn() {
        return groupFullNameCn;
    }

    public void setGroupFullNameCn(String groupFullNameCn) {
        this.groupFullNameCn = groupFullNameCn;
    }

    public String getgroupSimpleNameCn() {
        return groupSimpleNameCn;
    }

    public void setGroupSimpleNameCn(String groupSimpleNameCn) {
        this.groupSimpleNameCn = groupSimpleNameCn;
    }

    public String getGroupFullNameEn() {
        return groupFullNameEn;
    }

    public void setGroupFullNameEn(String groupFullNameEn) {
        this.groupFullNameEn = groupFullNameEn;
    }

    public String getGroupSimpleNameEn() {
        return groupSimpleNameEn;
    }

    public void setGroupSimpleNameEn(String groupSimpleNameEn) {
        this.groupSimpleNameEn = groupSimpleNameEn;
    }

    public String getGroupSimpleNameCn() {
        return groupSimpleNameCn;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
