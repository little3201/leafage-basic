/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.payment.service;

import top.abeille.basic.payment.entity.AccountInfo;
import top.abeille.common.basic.BasicService;

/**
 * 账户信息Service
 *
 * @author liwenqiang 2018/12/17 19:26
 **/
public interface AccountInfoService extends BasicService<AccountInfo> {

    AccountInfo getByAccountId(String accountId);

    void removeByAccountId(String accountId);
}
