package io.leafage.basic.hypervisor.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * dto class for dictionary.
 *
 * @author wq li 2022-04-06 17:33
 */
public class DictionaryDTO implements Serializable {

    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 简称
     */
    @NotBlank
    private String alias;
    /**
     * 上级
     */
    private Long superiorId;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
