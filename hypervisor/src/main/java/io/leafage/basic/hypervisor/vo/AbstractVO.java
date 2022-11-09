package io.leafage.basic.hypervisor.vo;

import java.time.LocalDateTime;

/**
 * desc
 *
 * @author wilsonli 2022/8/20 12:29
 **/
public abstract class AbstractVO<T> extends BasicVO<T> {

    private LocalDateTime modifyTime;

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
