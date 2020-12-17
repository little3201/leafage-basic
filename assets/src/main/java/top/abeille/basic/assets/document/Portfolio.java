/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for ArticleInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "portfolio")
public class Portfolio extends BaseDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
    /**
     * 标题
     */
    private String title;
    /**
     * url
     */
    private String url;
    /**
     * 类型：0-图片，1-视频
     */
    private char type;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

}
