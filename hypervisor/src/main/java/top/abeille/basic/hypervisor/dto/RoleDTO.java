/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

/**
 * Model class for RoleInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 2513250238715183575L;
    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 资源列表
     */
    private Set<RoleAuthorityDTO> sources;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<RoleAuthorityDTO> getSources() {
        return sources;
    }

    public void setSources(Set<RoleAuthorityDTO> sources) {
        this.sources = sources;
    }
}
