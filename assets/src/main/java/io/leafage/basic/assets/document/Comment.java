package io.leafage.basic.assets.document;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


/**
 * Model class for Comment
 *
 * @author liwenqiang 2021-07-15 22:12
 */
@Document(collection = "comment")
public class Comment extends AbstractDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
    /**
     * 帖子ID
     */
    @Indexed
    @Field(name = "posts_id")
    private ObjectId postsId;
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
     * 回复人
     */
    private String replier;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ObjectId getPostsId() {
        return postsId;
    }

    public void setPostsId(ObjectId postsId) {
        this.postsId = postsId;
    }

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

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }
}
