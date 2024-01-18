package io.leafage.basic.hypervisor.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * dto class for region.
 *
 * @author liwenqiang 2021-10-12 10:06
 */
public class RegionDTO implements Serializable {

    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 上级
     */
    private Long superiorId;
    /**
     * 邮编
     */
    private Integer postalCode;
    /**
     * 区号
     */
    private String areaCode;
    /**
     * 描述
     */
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
