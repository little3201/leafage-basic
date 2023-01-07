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
 * Model class for Comment
 *
 * @author liwenqiang 2021-07-15 22:12
 */
@Document(collection = "comment")
public class Comment extends AbstractDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
    /**
     * 帖子ID
     */
    @Indexed
    @Field(name = "posts_id")
    private ObjectId postsId;
    /**
     * 内容
     */
    private String content;
    /**
     * 国家
     */
    private String country;
    /**
     * 位置
     */
    private String location;
    /**
     * 回复人
     */
    private String replier;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }
}
