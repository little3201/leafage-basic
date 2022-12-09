package io.leafage.basic.hypervisor.vo;

/**
 * VO class for Notification
 *
 * @author liwenqiang 2022-02-10 13:53
 */
public class NotificationVO extends AbstractVO<String> {

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
