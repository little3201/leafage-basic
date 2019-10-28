/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.entity;

import javax.persistence.*;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 权限ID
     */
    @Column(name = "source_id")
    private String sourceId;
    /**
     * 上级
     */
    @Column(name = "superior")
    private String superior;
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 类型
     */
    @Column(name = "type")
    private Integer type;
    /**
     * 路径
     */
    @Column(name = "path")
    private String path;
    /**
     * 描述
     */
    @Column(name = "description")
    private String description;
    /**
     * 是否有效
     */
    @Column(name = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @Column(name = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
