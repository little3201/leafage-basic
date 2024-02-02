package io.leafage.basic.hypervisor.domain;

import io.leafage.basic.hypervisor.config.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * model class for message.
 *
 * @author liwenqiang 2022/1/29 17:20
 **/
@Entity
@Table(name = "messages", indexes = {@Index(name = "idx_messages_receiver", columnList = "receiver")})
public class Message extends AuditMetadata {

    /**
     * 主键
     */
    @Column(name = "name", nullable = false)
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否已读
     */
    @Column(name = "is_read", nullable = false)
    private boolean read;

    /**
     * 接收人
     */
    @Column(name = "receiver", nullable = false)
    private String receiver;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
