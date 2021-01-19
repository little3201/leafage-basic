/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户信息入参
 *
 * @author liwenqiang  2019-03-03 22:59
 **/
public class PostsDTO implements Serializable {

    private static final long serialVersionUID = -4116939329295119085L;

    /**
     * 标题
     */
    @NotBlank
    private String title;
    /**
     * 副标题
     */
    private String subtitle;
    /**
     * 分类
     */
    @NotNull
    private Long categoryId;
    /**
     * 封面
     */
    @NotBlank
    private String cover;
    /**
     * 内容
     */
    private String content;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
