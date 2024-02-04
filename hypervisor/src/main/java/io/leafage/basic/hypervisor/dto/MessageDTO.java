package io.leafage.basic.hypervisor.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * dto class for message.
 *
 * @author wq li 2022/1/29 17:20
 **/
public class MessageDTO implements Serializable {

    /**
     * 内容
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
