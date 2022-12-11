/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.dto;

import io.leafage.basic.assets.bo.ContentBO;
import io.leafage.basic.assets.bo.SuperBO;

/**
 * DTO class for Posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostDTO extends SuperBO {

    /**
     * 内容
     */
    private ContentBO content;

    public ContentBO getContent() {
        return content;
    }

    public void setContent(ContentBO content) {
        this.content = content;
    }
}
