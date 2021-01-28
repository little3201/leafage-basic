/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package io.leafage.basic.hypervisor.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Enter class for AccountInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 5424195124842285237L;
    /**
     * 余额
     */
    @NotNull
    private BigDecimal balance;
    /**
     * 类型
     */
    private int type;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
