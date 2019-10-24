/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Model class for SourceInfo
 *
 * @author liwenqiang
 */
public class SourceVO implements Serializable {

    /**
     * 权限ID
     */
    private String sourceId;
    /**
     * 上级
     */
    private String superior;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 路径
     */
    private String path;
    /**
     * 描述
     */
    private String description;
    /**
     * 修改人
     */
    private Long modifier;
    /**
     * 修改时间
     */
    private LocalDate modifyTime;


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

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public LocalDate getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDate modifyTime) {
        this.modifyTime = modifyTime;
    }
}
