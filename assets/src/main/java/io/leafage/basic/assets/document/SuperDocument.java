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
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

/**
 * Model class for posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public abstract class SuperDocument extends AbstractDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
    /**
     * 分类ID
     */
    @Field(name = "category_id")
    private ObjectId categoryId;
    /**
     * 标题
     */
    @TextIndexed
    private String title;
    /**
     * 封面
     */
    private String cover;
    /**
     * 标签
     */
    @TextIndexed
    private Set<String> tags;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ObjectId getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(ObjectId categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

}
