/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class for AccountInfo
 *
 * @author liwenqiang
 */
@Document(collection = "account_info")
public class AccountInfo {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 用户ID
     */
    @Field(value = "user_id")
    @Indexed
    private Long userId;
    /**
     * 账户ID
     */
    @Field(value = "account_id")
    @Indexed
    private Long accountId;
    /**
     * 余额
     */
    @Field(value = "balance")
    private BigDecimal balance;
    /**
     * 类型
     */
    @Field(value = "type")
    private String type;
    /**
     * 是否有效
     */
    @Field(value = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @Field(value = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @Field(value = "modify_time")
    private LocalDateTime modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
