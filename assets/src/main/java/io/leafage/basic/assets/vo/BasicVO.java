package io.leafage.basic.assets.vo;

/**
 * desc
 *
 * @author wilsonli 2022/8/20 12:28
 **/
public class BasicVO<T> {

    private T code;

    private String name;

    public T getCode() {
        return code;
    }

    public void setCode(T code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
