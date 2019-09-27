/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class for AccountInfo
 *
 * @author liwenqiang
 */
@Entity
@Table(name = "account_info")
public class AccountInfo {

    /**
     * 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    /**
     * 用户ID
     */
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    /**
     * 账户ID
     */
    @NotNull
    @Column(name = "account_id")
    private String accountId;
    /**
     * 余额
     */
    @Column(name = "balance")
    private BigDecimal balance;
    /**
     * 类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 是否有效
     */
    @JsonIgnore
    @Column(name = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人
     */
    @JsonIgnore
    @Column(name = "modifier")
    private Long modifier;
    /**
     * 修改时间
     */
    @JsonIgnore
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
