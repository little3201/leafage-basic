/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Model class for AccountInfo
 */
@Entity
@Table(name = "account_info")
public class AccountInfoModel {

    /**
     * 主键
     */
    private Long id;
    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 账户号码
     */
    private Long accountCode;
    /**
     * 账户余额
     */
    private BigDecimal accountBalance;
    /**
     * 账户类型
     */
    private String accountType;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 是否有效
     */
    @JsonIgnore
    private Boolean enabled;
    /**
     * 修改人ID
     */
    @JsonIgnore
    private Long modifierId;
    /**
     * 修改时间
     */
    @JsonIgnore
    private Date modifyTime;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "account_id")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Column(name = "account_code")
    public Long getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(Long accountCode) {
        this.accountCode = accountCode;
    }

    @Column(name = "account_type")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "account_balance")
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Column(name = "is_enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "modifier_id")
    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    @Column(name = "modify_time")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
