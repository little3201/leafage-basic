package top.abeille.basic.assets.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

public class BaseDocument {

    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 是否有效
     */
    @Field(value = "is_enabled")
    private boolean enabled = true;
    /**
     * 修改人
     */
    private String modifier;
    /**
     * 修改时间
     */
    @Field(value = "modify_time")
    @LastModifiedDate
    private LocalDateTime modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
