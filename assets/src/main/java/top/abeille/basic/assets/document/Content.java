/*
 * Copyright © 2010-2019 Everyday Chain. All rights reserved.
 */
package top.abeille.basic.assets.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * document for ArticleInfo
 *
 * @author liwenqiang
 */
@Document(collection = "article")
public class Content {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 文章ID
     */
    @Indexed
    private Long postsId;
    /**
     * 标题
     */
    @Indexed
    private String title;
    /**
     * 目录
     */
    private String category;
    /**
     * 内容
     */
    private String content;
    /**
     * 源文本——记录富文本或markdown原文
     */
    private String sourceText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPostsId() {
        return postsId;
    }

    public void setPostsId(Long postsId) {
        this.postsId = postsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }
}
