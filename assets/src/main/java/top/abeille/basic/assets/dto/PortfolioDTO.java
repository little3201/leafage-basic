/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * DTO for PortfolioInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PortfolioDTO implements Serializable {

    private static final long serialVersionUID = 6514393945624239153L;
    /**
     * 标题
     */
    @NotBlank
    private String title;
    /**
     * 副标题
     */
    @NotBlank
    private String subtitle;
    /**
     * 内容
     */
    @NotBlank
    private String content;
    /**
     * 封面
     */
    @NotBlank
    private String cover;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
