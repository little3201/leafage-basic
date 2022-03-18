package io.leafage.basic.assets.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO class for Record
 *
 * @author liwenqiang 2022-03-18 21:09
 */
public class RecordDTO {

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
