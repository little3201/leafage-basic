package io.leafage.basic.assets.vo;

import top.leafage.common.basic.AbstractVO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * VO class for Record
 *
 * @author liwenqiang 2022-03-18 21:09
 */
public class RecordVO extends AbstractVO<String> implements Serializable {

    private static final long serialVersionUID = -8300347588391830399L;

    /**
     * 日期
     */
    private LocalDate date;
    /**
     * cover
     */
    private String type;
    /**
     * items
     */
    private List<String> items;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
