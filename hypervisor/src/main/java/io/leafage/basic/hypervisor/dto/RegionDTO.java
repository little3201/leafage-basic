package io.leafage.basic.hypervisor.dto;

import java.io.Serializable;

/**
 * DTO class for Region
 *
 * @author liwenqiang 2021-10-12 10:06
 */
public class RegionDTO implements Serializable {

    private static final long serialVersionUID = -7556508514406968775L;
    /**
     * 代码
     */
    private Long code;
    /**
     * 名称
     */
    private String name;
    /**
     * 上级
     */
    private Long superior;
    /**
     * 邮编
     */
    private String zip;
    /**
     * 描述
     */
    private String description;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
