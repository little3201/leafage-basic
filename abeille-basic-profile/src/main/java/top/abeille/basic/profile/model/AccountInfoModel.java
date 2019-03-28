package top.abeille.basic.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Model class for AccountInfo
 */
@Entity
@Table(name = "account_info")
public class AccountInfoModel {

    //主键
    private Long id;
    //账户ID
    private String accountId;
    //账户号码
    private Long accountCode;
    //账户余额
    private BigDecimal accountBalance;
    //账户类型
    private String accountType;
    //用户ID
    private Long userId;
    //是否有效
    @JsonIgnore
    @NotNull
    private Boolean valid;
    //修改人ID
    @JsonIgnore
    @NotNull
    private Long modifierId;
    //修改时间
    @JsonIgnore
    @NotNull
    private Date modifyTime;

    /**
     * Get 主键
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Set 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get 账户ID
     */
    @Column(name = "account_id")
    public String getAccountId() {
        return accountId;
    }

    /**
     * Set 账户ID
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Get 账户号码
     */
    @Column(name = "account_code")
    public Long getAccountCode() {
        return accountCode;
    }

    /**
     * Set 账户号码
     */
    public void setAccountCode(Long accountCode) {
        this.accountCode = accountCode;
    }

    /**
     * Get 账户类型
     */
    @Column(name = "account_type")
    public String getAccountType() {
        return accountType;
    }

    /**
     * Set 账户类型
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * Get 用户ID
     */
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    /**
     * Set 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }


    /**
     * Get 账户余额
     */
    @Column(name = "account_balance")
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    /**
     * Set 账户余额
     */
    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    /**
     * Get 是否有效
     */
    @Column(name = "is_valid")
    public Boolean getValid() {
        return valid;
    }

    /**
     * Set 是否有效
     */
    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    /**
     * Get 修改人ID
     */
    @Column(name = "modifier_id")
    public Long getModifierId() {
        return modifierId;
    }

    /**
     * Set 修改人ID
     */
    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    /**
     * Get 修改时间
     */
    @Column(name = "modify_time")
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * Set 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}
