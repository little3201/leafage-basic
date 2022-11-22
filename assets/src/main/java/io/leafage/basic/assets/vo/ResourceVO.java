/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * VO for Resource
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class ResourceVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2168494818144125736L;

    /**
     * 编号
     */
    private String code;
    /**
     * 标题
     */
    private String title;
    /**
     * cover
     */
    private String cover;
    /**
     * 分类
     */
    private CategoryVO category;
    /**
     * 类型
     */
    private Character type;
    /**
     * 描述
     */
    private String description;
    /**
     * 点赞
     */
    private int downloads;
    /**
     * 查看
     */
    private int viewed;
    /**
     * 时间
     */
    private Instant modifyTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public CategoryVO getCategory() {
        return category;
    }

    public void setCategory(CategoryVO category) {
        this.category = category;
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

    public Instant getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Instant modifyTime) {
        this.modifyTime = modifyTime;
    }
}
