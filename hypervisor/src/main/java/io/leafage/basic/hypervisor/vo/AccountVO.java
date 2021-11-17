/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.vo;

import top.leafage.common.basic.AbstractVO;
import java.math.BigDecimal;

/**
 * VO class for Account
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class AccountVO extends AbstractVO<String> {

    private static final long serialVersionUID = 2227758644875658137L;

    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 类型
     */
    private char type;


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

}
