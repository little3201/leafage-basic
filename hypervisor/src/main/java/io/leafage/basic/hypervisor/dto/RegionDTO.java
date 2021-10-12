package io.leafage.basic.hypervisor.dto;

import java.io.Serializable;

public class RegionDTO implements Serializable {

    private static final long serialVersionUID = -7556508514406968775L;
    /**
     * 代码
     */
    private Long code;
    /**
     * 上级
     */
    private Long superior;
    /**
     * 名称
     */
    private String name;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getSuperior() {
        return superior;
    }

    public void setSuperior(Long superior) {
        this.superior = superior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
