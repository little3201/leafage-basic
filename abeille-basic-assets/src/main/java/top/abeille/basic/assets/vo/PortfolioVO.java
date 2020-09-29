/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.vo;

import top.abeille.basic.assets.bo.UserBO;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * VO for SourceInfo
 *
 * @author liwenqiang
 */
public class PortfolioVO implements Serializable {

    private static final long serialVersionUID = -2168494818144125736L;
    /**
     * 代码
     */
    private String code;
    /**
     * 作者
     */
    private UserBO author;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片url
     */
    private String imageUrl;
    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
