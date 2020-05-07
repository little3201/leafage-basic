/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

/**
 * Details VO class for TranslationInfo
 *
 * @author liwenqiang
 */
public class TranslationDetailsVO extends TranslationVO {

    private static final long serialVersionUID = 7989303162411272898L;
    /**
     * 内容
     */
    private String content;
    /**
     * 目录
     */
    private String catalog;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
}
