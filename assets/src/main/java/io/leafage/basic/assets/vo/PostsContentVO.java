/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.vo;

import java.io.Serial;
import java.io.Serializable;

/**
 * VO class for Posts Details
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostsContentVO extends PostsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3631862762916498067L;

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
