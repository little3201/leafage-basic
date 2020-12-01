/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import top.abeille.basic.assets.bo.UserTidyBO;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Enter class for ArticleInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class ArticleVO implements Serializable {

    private static final long serialVersionUID = -2692474466082844624L;
    /**
     * 代码
     */
    private String code;
    /**
     * 作者
     */
    private UserTidyBO author;
    /**
     * 标题
     */
    private String title;
    /**
     * 副标题
     */
    private String subtitle;
    /**
     * 图片url
     */
    private String imageUrl;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserTidyBO getAuthor() {
        return author;
    }

    public void setAuthor(UserTidyBO author) {
        this.author = author;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
