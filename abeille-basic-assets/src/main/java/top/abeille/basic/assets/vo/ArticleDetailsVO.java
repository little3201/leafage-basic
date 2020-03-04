/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

/**
 * Details VO class for ArticleInfo
 *
 * @author liwenqiang
 */
public class ArticleDetailsVO extends ArticleVO {

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
