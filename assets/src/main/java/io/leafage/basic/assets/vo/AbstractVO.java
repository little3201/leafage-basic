package io.leafage.basic.assets.vo;

import java.time.Instant;

/**
 * desc
 *
 * @author wilsonli 2022/8/20 12:29
 **/
public abstract class AbstractVO<T> extends BasicVO<T> {

    private Instant modifyTime;

    public Instant getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Instant modifyTime) {
        this.modifyTime = modifyTime;
    }
}
