/*
 * Copyright © 2010-2019 Abeille All rights reserved.
 */
package top.abeille.basic.hypervisor.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Outer class for AccountInfo
 *
 * @author liwenqiang 2020-10-06 22:09
 */
public class AccountVO implements Serializable {

    private static final long serialVersionUID = 2227758644875658137L;

    /**
     * 用户姓名
     */
    private String name;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 类型
     */
    private String type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
