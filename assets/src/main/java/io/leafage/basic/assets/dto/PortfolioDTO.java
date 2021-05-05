/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * DTO class for Portfolio
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PortfolioDTO implements Serializable {

    private static final long serialVersionUID = 6514393945624239153L;
    /**
     * 标题
     */
    @NotBlank
    @Size(max = 32)
    private String title;
    /**
     * url
     */
    @NotEmpty
    private Set<String> url;
    /**
     * 类型
     */
    private String type;
    /**
     * 分类
     */
    @NotBlank
    private String category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getUrl() {
        return url;
    }

    public void setUrl(Set<String> url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
