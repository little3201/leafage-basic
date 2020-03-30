/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.entity;

import javax.persistence.*;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private String businessId;
    /**
     * 标题
     */
    @Column(name = "title")
    private String title;
    /**
     * 概览
     */
    @Column(name = "summary")
    private String summary;
    /**
     * 图片url
     */
    @Column(name = "image_url")
    private String imageUrl;
    /**
     * 是否有效
     */
    @Column(name = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @Column(name = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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