/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

import top.abeille.basic.assets.bo.UserBO;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * VO class for TopicInfo
 *
 * @author liwenqiang
 */
public class TopicVO implements Serializable {

    private static final long serialVersionUID = 6078275280120953852L;
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
     * 内容
     */
    private String content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
