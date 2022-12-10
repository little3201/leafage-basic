/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;

/**
 * DTO class for Posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostDTO extends SuperDTO {

    /**
     * 内容
     */
    @NotBlank
    private String content;
    /**
     * 目录
     */
    private String catalog;


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

}
