package io.leafage.basic.hypervisor.vo;

/**
 * vo class for notification.
 *
 * @author liwenqiang 2022/1/29 17:20
 **/
public class MessageVO {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
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
