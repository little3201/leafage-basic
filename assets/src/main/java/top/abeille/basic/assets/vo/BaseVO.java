package top.abeille.basic.assets.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * VO base class
 *
 * @author liwenqiang  2019-03-03 22:59
 */
public class BaseVO implements Serializable {

    private static final long serialVersionUID = -2516015644852684341L;

    /**
     * 唯一标识
     */
    private String code;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
