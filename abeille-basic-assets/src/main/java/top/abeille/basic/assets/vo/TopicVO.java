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


    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 用户ID
     */
    private String userId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
