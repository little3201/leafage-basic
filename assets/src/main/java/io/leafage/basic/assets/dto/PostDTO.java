/*
 *  Copyright 2018-2023 the original author or authors.
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

package io.leafage.basic.assets.dto;

import io.leafage.basic.assets.bo.ContentBO;
import io.leafage.basic.assets.bo.PostBO;

/**
 * DTO class for Posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostDTO extends PostBO {

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
