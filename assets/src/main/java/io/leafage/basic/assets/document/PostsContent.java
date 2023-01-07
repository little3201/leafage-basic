/*
 *  Copyright 2018-2022 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
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
public class PostsContent extends AbstractDocument {

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
