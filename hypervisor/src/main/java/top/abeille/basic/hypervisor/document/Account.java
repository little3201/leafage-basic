/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

/**
 * Model class for Account
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "account")
public class Account extends BaseDocument {

    /**
     * 用户ID
     */
    @Field(value = "user_id")
    @Indexed
    private String userId;
    /**
     * 代码
     */
    @Indexed(unique = true)
    private String code;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 类型
     */
    private char type;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
