/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Id
    private String id;
    /**
     * 文章ID
     */
    @Field(value = "article_id")
    @Indexed
    private Long articleId;
    /**
     * 标题
     */
    @Field(value = "title")
    @Indexed
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
    @Field(value = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @Field(value = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @Field(value = "modify_time")
    private LocalDateTime modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
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
