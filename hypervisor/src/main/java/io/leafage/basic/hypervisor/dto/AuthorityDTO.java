/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Model class for SourceInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class AuthorityDTO implements Serializable {

    private static final long serialVersionUID = 8659525799803097800L;
    /**
     * 上级
     */
    private String superior;
    /**
     * 名称
     */
    @NotBlank
    @Size(max = 16)
    private String name;
    /**
     * 类型
     */
    @NotBlank
    private String type;
    /**
     * 路径
     */
    private String path;
    /**
     * 描述
     */
    private String description;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
}
