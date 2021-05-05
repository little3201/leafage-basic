/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for Posts Content
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
    private ObjectId postsId;
    /**
     * 内容
     */
    private String content;
    /**
     * 目录
     */
    private String catalog;


    public ObjectId getPostsId() {
        return postsId;
    }

    public void setPostsId(ObjectId postsId) {
        this.postsId = postsId;
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
