/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Model class for AccountInfo
 *
 * @author liwenqiang
 */
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 2516536769852195479L;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 类型
     */
    private String type;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

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
