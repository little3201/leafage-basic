/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import io.leafage.basic.assets.bo.ContentBO;

/**
 * VO class for Posts Details
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostContentVO extends PostVO {

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
