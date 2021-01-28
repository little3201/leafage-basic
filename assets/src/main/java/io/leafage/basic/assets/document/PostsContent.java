/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package io.leafage.basic.assets.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for ContentInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "posts_content")
public class PostsContent extends BaseDocument {

    /**
     * 帖子ID
     */
    @Field(value = "posts_id")
    @Indexed(unique = true)
    private String postsId;
    /**
     * 原文
     */
    private String original;
    /**
     * 内容
     */
    private String content;
    /**
     * 目录
     */
    private String catalog;


    public String getPostsId() {
        return postsId;
    }

    public void setPostsId(String postsId) {
        this.postsId = postsId;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

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
