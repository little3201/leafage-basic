package io.leafage.basic.assets.entity;

import javax.persistence.Column;

/**
 * assets abstract entity
 *
 * @author liwenqiang 2022/4/28 17:06
 **/
public class AssetsAbstractEntity extends AbstractEntity {

    /**
     * 唯一标识
     */
    @Column(unique = true)
    private String code;
    /**
     * 标题
     */
    private String title;
    /**
     * 分类
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    /**
     * 封面
     */
    private String cover;
    /**
     * 查看
     */
    private int viewed;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }
}
