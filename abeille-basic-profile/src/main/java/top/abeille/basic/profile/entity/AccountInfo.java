/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile.entity;

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
     * 账户号码
     */
    @Column(name = "account_code")
    private Long accountCode;
    /**
     * 账户余额
     */
    @Column(name = "account_balance")
    private BigDecimal accountBalance;
    /**
     * 账户类型
     */
    @Column(name = "account_type")
    private String accountType;

    /**
     * 是否有效
     */
    @JsonIgnore
    @Column(name = "is_enabled")
    private Boolean enabled;
    /**
     * 修改人ID
     */
    @JsonIgnore
    @Column(name = "modifier_id")
    private Long modifierId;
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

    public Long getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(Long accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
