/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import top.abeille.basic.assets.bo.UserTidyBO;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * VO for PortfolioInfo
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
    private UserTidyBO author;
    /**
     * 标题
     */
    private String title;
    /**
     * url
     */
    private String url;
    /**
     * 类型
     */
    private char type;
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

    public UserTidyBO getAuthor() {
        return author;
    }

    public void setAuthor(UserTidyBO author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
