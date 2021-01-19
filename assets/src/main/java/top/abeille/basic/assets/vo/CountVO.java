package top.abeille.basic.assets.vo;

import java.io.Serializable;

/**
 * 统计类
 */
public class CountVO implements Serializable {

    private static final long serialVersionUID = -6407149385665045323L;

    /**
     * 唯一标识
     */
    private String code;
    /**
     * 统计数
     */
    private int count;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
