/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

import top.abeille.basic.assets.api.bo.UserBO;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * VO class for TranslationInfo
 *
 * @author liwenqiang
 */
public class TranslationVO implements Serializable {

    private static final long serialVersionUID = -3749575111922683487L;
    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 作者
     */
    private UserBO author;
    /**
     * 标题
     */
    private String title;
    /**
     * 原文连接
     */
    private String originalUrl;
    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public UserBO getAuthor() {
        return author;
    }

    public void setAuthor(UserBO author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
