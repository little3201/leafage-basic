/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

import java.io.Serializable;

/**
 * VO class for TopicInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class CategoryVO implements Serializable {

    private static final long serialVersionUID = 6078275280120953852L;
    /**
     * 代码
     */
    private String code;
    /**
     * 别名
     */
    private String alias;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
