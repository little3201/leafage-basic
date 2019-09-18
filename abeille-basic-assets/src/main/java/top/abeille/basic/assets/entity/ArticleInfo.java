/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Model class for ArticleInfo
 *
 * @author liwenqiang
 */
@Document(collection = "article_info")
public class ArticleInfo {

    /**
     * 主键
     */
    @JsonIgnore
    @Id
    private Long id;
    /**
     * 文章ID
     */
    @NotNull
    @Field(value = "article_id")
    private String articleId;
    /**
     * 标题
     */
    @Field(value = "title")
    private String title;
    /**
     * 描述
     */
    @Field(value = "description")
    private String description;
    /**
     * 内容
     */
    @Field(value = "content")
    private String content;
    /**
     * 图片url
     */
    @Field(value = "image_url")
    private String imageUrl;
    /**
     * 是否有效
     */
    @JsonIgnore
    @Field(value = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @JsonIgnore
    @Field(value = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @JsonIgnore
    @Field(value = "modify_time")
    private LocalDateTime modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
