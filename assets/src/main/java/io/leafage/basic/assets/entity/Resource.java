package io.leafage.basic.assets.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for resource.
 *
 * @author liwenqiang  2021-09-29 10:45
 */
@Entity
@Table(name = "resource")
public class Resource extends AbstractEntity {

    /**
     * 代码
     */
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    /**
     * 标题
     */
    private String title;
    /**
     * 分类
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    /**
     * cover
     */
    private String cover;
    /**
     * 类型
     */
    private Character type;
    /**
     * 下载量
     */
    private int downloads;
    /**
     * 查看
     */
    private int viewed;
    /**
     * 描述
     */
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
