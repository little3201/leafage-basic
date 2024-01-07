/*
 *  Copyright 2018-2024 the original author or authors.
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

package io.leafage.basic.hypervisor.bo;


import jakarta.validation.constraints.NotBlank;

/**
 * DTO class for Notification
 *
 * @author liwenqiang 2022-12-10 13:49
 */
public abstract class MessageBO {

    /**
     * 标题
     */
    @NotBlank(message = "title cannot be blank.")
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "context cannot be blank.")
    private String context;

    /**
     * 接收人
     */
    @NotBlank(message = "receiver cannot be blank.")
    private String receiver;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
