/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class for AccountInfo
 *
 * @author liwenqiang
 */
public class AccountVO implements Serializable {

    private static final long serialVersionUID = 6540470230706397453L;
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
