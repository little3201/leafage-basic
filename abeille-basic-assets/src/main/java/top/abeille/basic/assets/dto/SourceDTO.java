/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.dto;

import javax.validation.constraints.NotBlank;

/**
 * DTO for SourceInfo
 *
 * @author liwenqiang
 */
public class SourceDTO {

    /**
     * 用户ID
     */
    private String userId;
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
     * 内容
     */
    @NotBlank
    private String content;
    /**
     * 图片url
     */
    @NotBlank
    private String imageUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
