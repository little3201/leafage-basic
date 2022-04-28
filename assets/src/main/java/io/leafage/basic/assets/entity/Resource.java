package io.leafage.basic.assets.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class for resource.
 *
 * @author liwenqiang  2021-09-29 10:45
 */
@Entity
@Table(name = "resource")
public class Resource extends AssetsAbstractEntity {

    /**
     * 类型
     */
    private Character type;
    /**
     * 下载量
     */
    private int downloads;
    /**
     * 描述
     */
    private String description;


    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
