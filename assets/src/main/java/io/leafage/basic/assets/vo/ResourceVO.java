package io.leafage.basic.assets.vo;

import top.leafage.common.basic.AbstractVO;

/**
 * VO class for resource.
 *
 * @author liwenqiang  2019-03-03 22:59
 **/
public class ResourceVO extends AbstractVO<String> {

    private static final long serialVersionUID = 5927331601304060786L;

    /**
     * 标题
     */
    private String title;
    /**
     * 分类
     */
    private String category;
    /**
     * cover
     */
    private String cover;
    /**
     * 类型
     */
    private Character type;
    /**
     * 描述
     */
    private String description;
    /**
     * 下载量
     */
    private int downloads;
    /**
     * 查看
     */
    private int viewed;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }
}
