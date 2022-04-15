package io.leafage.basic.assets.vo;

/**
 * VO class for posts content.
 *
 * @author liwenqiang  2022-04-15 11:21
 **/
public class ContentVO {

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
