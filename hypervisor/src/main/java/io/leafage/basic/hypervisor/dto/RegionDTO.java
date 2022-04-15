package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO class for region.
 *
 * @author liwenqiang 2021-10-12 10:06
 */
public class RegionDTO implements Serializable {

    private static final long serialVersionUID = -7556508514406968775L;

    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 上级
     */
    private Long superior;
    /**
     * 邮编
     */
    private String postalCode;
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

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
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
