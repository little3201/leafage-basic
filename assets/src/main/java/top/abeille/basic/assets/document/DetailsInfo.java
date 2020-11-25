/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Model class for ContentInfo
 *
 * @author liwenqiang
 */
@Document(collection = "details_info")
public class DetailsInfo {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 文章ID
     */
    @Field(value = "article_id")
    @Indexed(unique = true)
    private String articleId;
    /**
     * 原文
     */
    private String original;
    /**
     * 内容
     */
    private String content;
    /**
     * 目录
     */
    private String catalog;
    /**
     * 是否有效
     */
    private boolean enabled;
    /**
     * 修改人
     */
    private String modifier;
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

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
