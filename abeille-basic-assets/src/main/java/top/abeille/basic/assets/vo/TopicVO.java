/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

import java.io.Serializable;

/**
 * VO class for TopicInfo
 *
 * @author liwenqiang
 */
public class TopicVO implements Serializable {

    private static final long serialVersionUID = 6078275280120953852L;
    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
}
