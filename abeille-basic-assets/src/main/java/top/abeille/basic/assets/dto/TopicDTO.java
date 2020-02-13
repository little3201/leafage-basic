/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.assets.dto;

/**
 * DTO class for TopicInfo
 *
 * @author liwenqiang
 */
public class TopicDTO {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 内容
     */
    private String content;

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
