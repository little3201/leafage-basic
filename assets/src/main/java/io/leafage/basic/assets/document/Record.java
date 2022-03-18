package io.leafage.basic.assets.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

/**
 * Model class for Record
 *
 * @author liwenqiang 2022-03-18 21:09
 */
@Document(collection = "record")
public class Record extends AbstractDocument {

    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
