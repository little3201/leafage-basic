package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;
import java.io.Serializable;

/**
 * VO class for notification.
 *
 * @author liwenqiang 2022/1/29 17:20
 **/
public class NotificationVO extends AbstractVO<String> implements Serializable {

    private static final long serialVersionUID = -1492628836670996944L;

    /**
     * 内容
     */
    private String content;
    /**
     * 接收人
     */
    private String receiver;


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
