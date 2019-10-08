/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.vo.outer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Outer class for AccountInfo
 *
 * @author liwenqiang
 */
public class AccountOuter implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 账户ID
     */
    private Long accountId;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 类型
     */
    private String type;
    /**
     * 修改人
     */
    private Long modifier;
    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
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

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
