/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Model class for SourceInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "source_info")
public class SourceInfo {

    /**
     * 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 权限编号
     */
    @NotNull
    @Column(name = "source_code")
    private String sourceCode;
    /**
     * 权限父编号
     */
    @Column(name = "source_parent_code")
    private String sourceParentCode;
    /**
     * 权限中文名称
     */
    @Column(name = "source_name_cn")
    private String sourceNameCn;
    /**
     * 权限英文名称
     */
    @Column(name = "source_name_en")
    private String sourceNameEn;
    /**
     * 权限类型
     */
    @Column(name = "source_type")
    private Integer sourceType;
    /**
     * 权限路径
     */
    @Column(name = "source_path")
    private String sourcePath;
    /**
     * 权限描述
     */
    @Column(name = "source_desc")
    private String sourceDesc;

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

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceParentCode() {
        return sourceParentCode;
    }

    public void setSourceParentCode(String sourceParentCode) {
        this.sourceParentCode = sourceParentCode;
    }

    public String getSourceNameCn() {
        return sourceNameCn;
    }

    public void setSourceNameCn(String sourceNameCn) {
        this.sourceNameCn = sourceNameCn;
    }

    public String getSourceNameEn() {
        return sourceNameEn;
    }

    public void setSourceNameEn(String sourceNameEn) {
        this.sourceNameEn = sourceNameEn;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
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
