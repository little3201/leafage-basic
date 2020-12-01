/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */

package top.abeille.basic.assets.vo;

import java.io.Serializable;

/**
 * 统计对象
 *
 * @author liwenqiang 2020-10-06 22:09
 **/
public class StatisticsVO implements Serializable {

    private static final long serialVersionUID = 5080919936022801892L;

    private int label;

    private long value;

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
