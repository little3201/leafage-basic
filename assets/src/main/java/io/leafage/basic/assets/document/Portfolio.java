/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Model class for Portfolio
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "portfolio")
public class Portfolio extends BaseDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
    /**
     * 标题
     */
    private String title;
    /**
     * url
     */
    private Set<String> url;
    /**
     * 类型
     */
    private String type;
    /**
     * 点赞
     */
    private int likes;
    /**
     * 评论
     */
    private int comment;
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

    public Set<String> getUrl() {
        return url;
    }

    public void setUrl(Set<String> url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
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
