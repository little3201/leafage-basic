package io.leafage.basic.assets.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO class for Resource
 *
 * @author liwenqiang 2021-09-29 13:51
 */
public class ResourceDTO implements Serializable {

    private static final long serialVersionUID = -6879604484440257806L;

    /**
     * 标题
     */
    @NotBlank
    @Size(max = 32)
    private String title;
    /**
     * cover
     */
    @NotBlank
    private String cover;
    /**
     * 类型
     */
    @NotNull
    private Character type;
    /**
     * 描述
     */
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
