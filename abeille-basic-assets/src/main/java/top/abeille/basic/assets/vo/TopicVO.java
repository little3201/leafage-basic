/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.vo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * VO class for TopicInfo
 *
 * @author liwenqiang
 */
public class TopicVO {

    /**
     * 话题ID
     */
    private String topicId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 内容
     */
    private String content;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
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
