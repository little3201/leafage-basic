/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

/**
 * VO class for Posts Details
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostContentVO extends PostVO {

    /**
     * 内容
     */
    private ContentVO content;

    public ContentVO getContent() {
        return content;
    }

    public void setContent(ContentVO content) {
        this.content = content;
    }
}
