package io.leafage.basic.assets.vo;

import top.leafage.common.basic.AbstractVO;
import java.io.Serializable;

/**
 * VO class for Comment
 *
 * @author liwenqiang 2021-07-15 22:12
 */
public class CommentVO extends AbstractVO<String> implements Serializable {

    private static final long serialVersionUID = -3606281697452944193L;

    /**
     * 内容
     */
    private String content;
    /**
     * 国家
     */
    private String country;
    /**
     * 位置
     */
    private String location;
    /**
     * 回复数
     */
    private long count;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
