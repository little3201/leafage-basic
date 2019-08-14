/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class for ArticleInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "article_info")
public class ArticleInfo {

    /**
     * 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    /**
     * 文章ID
     */
    @NotNull
    @Column(name = "article_id")
    private String articleId;
    /**
     * 文章标题
     */
    @Column(name = "article_title")
    private Long articleTitle;
    /**
     * 文章描述
     */
    @Column(name = "article_content")
    private BigDecimal articleContent;
    /**
     * 文章描述
     */
    @Column(name = "article_description")
    private BigDecimal articleDescription;
    /**
     * 文章url
     */
    @Column(name = "article_url")
    private String articleUrl;
    /**
     * 主题图片url
     */
    @Column(name = "article_image_url")
    private String articleImageUrl;
    /**
     * 是否有效
     */
    @JsonIgnore
    @Column(name = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人ID
     */
    @JsonIgnore
    @Column(name = "modifier_id")
    private Long modifierId;
    /**
     * 修改时间
     */
    @JsonIgnore
    @Column(name = "modify_time")
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

    public Long getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(Long articleTitle) {
        this.articleTitle = articleTitle;
    }

    public BigDecimal getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(BigDecimal articleContent) {
        this.articleContent = articleContent;
    }

    public BigDecimal getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(BigDecimal articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getArticleImageUrl() {
        return articleImageUrl;
    }

    public void setArticleImageUrl(String articleImageUrl) {
        this.articleImageUrl = articleImageUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
