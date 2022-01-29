package io.leafage.basic.hypervisor.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for notification.
 *
 * @author liwenqiang 2022/1/29 17:20
 **/
@Entity
@Table(name = "notification")
public class Notification extends AbstractEntity {

    /**
     * 代码
     */
    private String code;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否已读
     */
    private boolean read;
    /**
     * 接收人
     */
    private String receiver;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
