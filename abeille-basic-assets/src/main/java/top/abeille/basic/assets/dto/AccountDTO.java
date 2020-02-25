/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Enter class for AccountInfo
 *
 * @author liwenqiang
 */
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 5424195124842285237L;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 类型
     */
    private String type;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
