package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotBlank;

/**
 * DTO class for Notification
 *
 * @author liwenqiang 2022-02-10 13:49
 */
public class NotificationDTO {

    /**
     * 标题
     */
    @NotBlank
    private String title;
    /**
     * 内容
     */
    @NotBlank
    private String content;
    /**
     * 接收人
     */
    @NotBlank
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
