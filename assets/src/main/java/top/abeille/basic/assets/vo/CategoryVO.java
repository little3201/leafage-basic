/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

/**
 * VO class for TopicInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class CategoryVO extends BaseVO {

    private static final long serialVersionUID = 6078275280120953852L;
    /**
     * 别名
     */
    private String alias;


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
