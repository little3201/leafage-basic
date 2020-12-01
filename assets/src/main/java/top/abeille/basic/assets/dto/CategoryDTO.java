/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO class for CategoryInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = -6972802402513706361L;
    /**
     * 别名
     */
    @NotBlank
    private String alias;
    /**
     * 修改人
     */
    @NotBlank
    private String modifier;


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
