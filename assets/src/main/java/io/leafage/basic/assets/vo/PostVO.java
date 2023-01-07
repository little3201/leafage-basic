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

package io.leafage.basic.assets.vo;

import io.leafage.basic.assets.bo.ContentBO;
import io.leafage.basic.assets.bo.SuperBO;

import java.time.LocalDateTime;

/**
 * VO class for Posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class PostVO extends SuperBO {

    /**
     * 编号
     */
    private String code;
    /**
     * 内容和目录
     */
    private ContentBO content;
    /**
     * 时间
     */
    private LocalDateTime modifyTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ContentBO getContent() {
        return content;
    }

    public void setContent(ContentBO content) {
        this.content = content;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
