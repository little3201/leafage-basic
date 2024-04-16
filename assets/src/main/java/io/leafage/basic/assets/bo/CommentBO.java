/*
 *  Copyright 2018-2024 little3201.
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

package io.leafage.basic.assets.bo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * bo class for comment
 *
 * @author liwenqiang 2023-03-26 14:30
 */
public abstract class CommentBO {

    /**
     * 帖子
     */
    @NotNull(message = "postId must not be null.")
    private Long postId;

    /**
     * 内容
     */
    @NotBlank(message = "content must not be blank.")
    private String content;

    /**
     * 回复者
     */
    private Long replier;


    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReplier() {
        return replier;
    }

    public void setReplier(Long replier) {
        this.replier = replier;
    }
}
