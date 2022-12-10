/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.bo;

/**
 * VO class for posts content
 *
 * @author liwenqiang 2021-02-26 22:17
 */
public class ContentBO {

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
