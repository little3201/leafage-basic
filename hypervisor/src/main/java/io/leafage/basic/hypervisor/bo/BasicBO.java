package io.leafage.basic.hypervisor.bo;

/**
 * BO class
 *
 * @author wilsonli 2022-12-09 22:55
 **/
public class BasicBO<T> {

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
