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

package io.leafage.basic.hypervisor.domain;

import io.leafage.basic.hypervisor.audit.AuditMetadata;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * model class for message
 *
 * @author wq li
 */
@Table(name = "messages")
public class Message extends AuditMetadata {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String context;
    /**
     * 是否已读
     */
    @Column(value = "is_read")
    private boolean read;
    /**
     * 接收人
     */
    private String receiver;


    /**
     * <p>Getter for the field <code>title</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Setter for the field <code>title</code>.</p>
     *
     * @param title a {@link java.lang.String} object
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * <p>Getter for the field <code>context</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getContext() {
        return context;
    }

    /**
     * <p>Setter for the field <code>context</code>.</p>
     *
     * @param context a {@link java.lang.String} object
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * <p>isRead.</p>
     *
     * @return a boolean
     */
    public boolean isRead() {
        return read;
    }

    /**
     * <p>Setter for the field <code>read</code>.</p>
     *
     * @param read a boolean
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * <p>Getter for the field <code>receiver</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * <p>Setter for the field <code>receiver</code>.</p>
     *
     * @param receiver a {@link java.lang.String} object
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

}
