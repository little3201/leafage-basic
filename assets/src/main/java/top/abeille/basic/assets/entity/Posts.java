/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for Posts
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "posts")
public class Posts extends BaseEntity {

    /**
     * 业务ID
     */
    private String code;
    /**
     * 标题
     */
    private String title;
    /**
     * 概览
     */
    private String subtitle;
    /**
     * 图片url
     */
    private String cover;


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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

}
